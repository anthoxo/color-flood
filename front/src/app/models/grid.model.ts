export interface GridResultDto {
  history: EnrichedGrid[];
  statistics: Statistic[];
}

export type Joker = 'ZAP' | 'SHADOW' | 'SHIELD' | 'ARCANE_THIEF' | 'NONE';

export interface EnrichedGrid {
  grid: Grid;
  usedJoker: Joker;
}

export interface Grid {
  colors: number[][];
}

export type StartingTile = 'TOP_LEFT' | 'BOTTOM_RIGHT' | 'BOTTOM_LEFT' | 'TOP_RIGHT';

export interface Statistic {
  name: string;
  rank: number;
  finalColor: number;
  tileNumber: number;
  startingTile: StartingTile;
}
