export interface GameOverviewDto {
  soloGames: GameDto[];
  versusGames: GameDto[];
  battleGames: GameDto[];
}

interface GameDto {
  id: number;
  gridId: number;
  players: string[]
}