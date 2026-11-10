"""Confere somente os números ilustrativos do protótipo, sem executar o app."""
import json
import math
from pathlib import Path


def ahp(matrix):
    gm = [math.prod(row) ** (1 / len(row)) for row in matrix]
    weights = [value / sum(gm) for value in gm]
    lam = sum(sum(a * b for a, b in zip(row, weights)) / w
              for row, w in zip(matrix, weights)) / len(weights)
    ci = (lam - len(weights)) / (len(weights) - 1)
    return {"pesos": weights, "lambda": lam, "ic": ci, "ri": 1.12, "rc": ci / 1.12}


def topsis(values, weights, benefits):
    norms = [math.sqrt(sum(row[j] ** 2 for row in values)) for j in range(5)]
    if any(n == 0 for n in norms):
        return {"tipo": "falha", "motivo": "Coluna com norma zero"}
    v = [[x / n * w for x, n, w in zip(row, norms, weights)] for row in values]
    positive = [(max if benefit else min)(row[j] for row in v)
                for j, benefit in enumerate(benefits)]
    negative = [(min if benefit else max)(row[j] for row in v)
                for j, benefit in enumerate(benefits)]
    result = []
    for i, row in enumerate(v):
        dp = math.dist(row, positive)
        dm = math.dist(row, negative)
        result.append({"alternativa": i, "dMais": dp, "dMenos": dm,
                       "coeficiente": dm / (dp + dm) if dp + dm else None})
    if all(r["coeficiente"] is None for r in result):
        return {"tipo": "sem-diferenciacao", "alternativas": result}
    result.sort(key=lambda r: -r["coeficiente"])
    leader = None
    for index, row in enumerate(result, 1):
        if leader is None or abs(leader - row["coeficiente"]) > 1e-9:
            leader, rank = row["coeficiente"], index
        row["posicao"] = rank
    return {"tipo": "ranking", "alternativas": result}


def validate(data):
    for scenario in data["cenarios"]:
        calculated = ahp(scenario["matrizAhp"])
        assert all(math.isclose(calculated[k], scenario["ahp"][k], abs_tol=1e-9)
                   for k in ["lambda", "ic", "rc"]), scenario["id"]
        assert all(math.isclose(a, b, abs_tol=1e-9)
                   for a, b in zip(calculated["pesos"], scenario["ahp"]["pesos"]))
        if "topsis" in scenario:
            assert calculated["rc"] <= 0.10
            assert topsis(scenario["valores"], calculated["pesos"], scenario.get("beneficios", data["beneficios"])) == scenario["topsis"]
    by_id = {s["id"]: s for s in data["cenarios"]}
    assert by_id["inconsistente"]["ahp"]["rc"] > 0.10
    assert [r["posicao"] for r in by_id["empate"]["topsis"]["alternativas"]] == [1, 2, 2, 4]
    assert by_id["sem-diferenciacao"]["topsis"]["tipo"] == "sem-diferenciacao"
    assert by_id["falha"]["topsis"]["tipo"] == "falha"
    print(f'{len(data["cenarios"])} cenários ilustrativos conferidos; não são testes dos motores Android.')


if __name__ == "__main__":
    validate(json.loads(Path(__file__).with_name("dados-demonstracao.json").read_text()))
