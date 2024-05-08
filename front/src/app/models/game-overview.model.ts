export interface GameOverviewDto {
  soloGames: GameDto[];
  versusGames: GameDto[];
  battleGames: GameDto[];
}

interface GameDto {
  id: number;
  players: string[]
}