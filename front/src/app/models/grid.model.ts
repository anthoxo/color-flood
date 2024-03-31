export interface GridResultDto {
  history: Grid[];
  statistics: Statistic[];
}

export interface Grid {
  colors: number[][];
}

export interface Statistic {
  name: string;
  finalColor: number;
}