export interface GridResultDto {
  history: Grid[];
  statistics: Statistic[];
}

export interface Grid {
  colors: number[][];
}

export type StartingTile = 'TOP_LEFT' | 'BOTTOM_RIGHT';

export interface Statistic {
  name: string;
  rank: number;
  finalColor: number;
  tileNumber: number;
  startingTile: StartingTile;
}
