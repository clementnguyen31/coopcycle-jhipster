import { IClient } from 'app/entities/client/client.model';

export interface IRecherche {
  id?: number;
  rechercheId?: number | null;
  rechercheThematique?: string | null;
  rechercheGeographique?: string | null;
  clients?: IClient[] | null;
}

export class Recherche implements IRecherche {
  constructor(
    public id?: number,
    public rechercheId?: number | null,
    public rechercheThematique?: string | null,
    public rechercheGeographique?: string | null,
    public clients?: IClient[] | null
  ) {}
}

export function getRechercheIdentifier(recherche: IRecherche): number | undefined {
  return recherche.id;
}
