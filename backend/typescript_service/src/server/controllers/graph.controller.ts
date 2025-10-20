import { type Request, type Response } from 'express';
import { linhasDoMapa } from '../../mapa.js';

type NomeLinha = (typeof linhasDoMapa)[number]['linha'];
type Estacao = string;

const TEMPO_BALDEACAO = 4; // minutos

const temposPorLinha: Record<NomeLinha, number> = {
  'Linha 1 - Azul': 2.0,
  'Linha 2 - Verde': 2.2,
  'Linha 3 - Vermelha': 2.0,
  'Linha 4 - Amarela': 2.5,
  'Linha 5 - Lilás': 2.3,
  'Linha 7 - Rubi': 5.0,
  'Linha 8 - Diamante': 4.5,
  'Linha 9 - Esmeralda': 3.5,
  'Linha 10 - Turquesa': 5.5,
  'Linha 11 - Coral': 6.0,
  'Linha 12 - Safira': 5.0,
  'Linha 13 - Jade': 12.0,
  'Linha 15 - Prata': 1.8,
};

const tempoPausaPorLinha: Record<NomeLinha, number> = {
  'Linha 1 - Azul': 0.6,
  'Linha 2 - Verde': 0.6,
  'Linha 3 - Vermelha': 0.6,
  'Linha 4 - Amarela': 0.5,
  'Linha 5 - Lilás': 0.6,
  'Linha 7 - Rubi': 0.8,
  'Linha 8 - Diamante': 0.8,
  'Linha 9 - Esmeralda': 0.7,
  'Linha 10 - Turquesa': 0.8,
  'Linha 11 - Coral': 0.8,
  'Linha 12 - Safira': 0.8,
  'Linha 13 - Jade': 0.7,
  'Linha 15 - Prata': 0.5,
};

interface Conexao { estacao: Estacao; linha: NomeLinha }

const grafo: Map<Estacao, Conexao[]> = new Map();

function adicionarAresta(estacao1: Estacao, estacao2: Estacao, linha: NomeLinha) {
  if (!grafo.has(estacao1)) grafo.set(estacao1, []);
  grafo.get(estacao1)!.push({ estacao: estacao2, linha });
}

linhasDoMapa.forEach(({ linha, estacoes }) => {
  for (let i = 0; i < estacoes.length; i++) {
    const atual = estacoes[i];
    if (i > 0) adicionarAresta(atual, estacoes[i - 1], linha as NomeLinha);
    if (i < estacoes.length - 1) adicionarAresta(atual, estacoes[i + 1], linha as NomeLinha);
  }
});

function encontrarCaminho(partida: Estacao, chegada: Estacao): Estacao[] | null {
  if (!grafo.has(partida) || !grafo.has(chegada)) return null;

  const fila: Estacao[][] = [[partida]];
  const visitados = new Set<Estacao>([partida]);

  while (fila.length > 0) {
    const caminho = fila.shift()!;
    const ultima = caminho[caminho.length - 1];

    if (ultima === chegada) return caminho;

    const vizinhos = grafo.get(ultima) ?? [];
    for (const { estacao: vizinho } of vizinhos) {
      if (!visitados.has(vizinho)) {
        visitados.add(vizinho);
        fila.push([...caminho, vizinho]);
      }
    }
  }

  return null;
}

function formatarTempo(minutos: number) {
  const mins = Math.round(minutos);
  if (mins < 60) {
    return `${mins} minuto${mins !== 1 ? 's' : ''}`;
  }
  const horas = Math.floor(mins / 60);
  const minutosRestantes = mins % 60;
  return `${horas}h${minutosRestantes > 0 ? ` ${minutosRestantes}min` : ''}`;
}

function detalharCaminho(caminho: Estacao[] | null) {
  if (!caminho) return null;

  const caminhoDetalhado: { estacao: Estacao; linha: NomeLinha }[] = [];
  const baldeacoes: { estacao: Estacao; sairDa: NomeLinha; entrarNa: NomeLinha }[] = [];
  let tempoTotal = 0;

  const getLinha = (a: Estacao, b: Estacao): NomeLinha => {
    const conexoes = grafo.get(a)!;
    return conexoes.find((c) => c.estacao === b)!.linha;
  };

  let linhaAtual = getLinha(caminho[0], caminho[1]);
  caminhoDetalhado.push({ estacao: caminho[0], linha: linhaAtual });

  for (let i = 1; i < caminho.length; i++) {
    const anterior = caminho[i - 1];
    const atual = caminho[i];
    const linhaProx = getLinha(anterior, atual);

    tempoTotal += temposPorLinha[linhaProx] ?? 3;

    if (i > 1) {
      tempoTotal += tempoPausaPorLinha[linhaAtual] ?? 0.6;
    }

    if (linhaProx !== linhaAtual) {
      tempoTotal += TEMPO_BALDEACAO;
      baldeacoes.push({ estacao: anterior, sairDa: linhaAtual, entrarNa: linhaProx });
      linhaAtual = linhaProx;
    }

    caminhoDetalhado.push({ estacao: atual, linha: linhaAtual });
  }

  if (caminho.length > 1) {
    const ultimaLinha = caminhoDetalhado[caminhoDetalhado.length - 1].linha;
    tempoTotal += tempoPausaPorLinha[ultimaLinha] ?? 0.6;
  }

  return {
    caminho: caminhoDetalhado,
    baldeacoes,
    estatisticas: {
      numeroEstacoes: caminho.length,
      numeroBaldeacoes: baldeacoes.length,
      tempoEstimadoMinutos: Math.round(tempoTotal),
      tempoEstimadoFormatado: formatarTempo(tempoTotal),
    },
  } as const;
}

export function getRoute(req: Request, res: Response) {
  const query = req.query as any;
  const partida = query.partida as string | undefined;
  const chegada = query.chegada as string | undefined;

  if (!partida || !chegada) {
    return res
      .status(400)
      .json({ erro: 'Parâmetros "partida" e "chegada" são obrigatórios.' });
  }

  const todasEstacoes = new Set(linhasDoMapa.flatMap((l) => l.estacoes));
  if (!todasEstacoes.has(partida) || !todasEstacoes.has(chegada)) {
    return res
      .status(404)
      .json({ erro: 'Uma ou ambas as estações não foram encontradas.' });
  }

  const caminhoSimples = encontrarCaminho(partida, chegada);
  const resultado = detalharCaminho(caminhoSimples);

  if (!resultado) {
    return res.status(404).json({
      erro: `Não foi possível encontrar um caminho entre ${partida} e ${chegada}.`,
    });
  }

  res.json(resultado);
}


