import { ICooperative } from 'app/entities/cooperative/cooperative.model';

export interface ILivreur {
  id?: number;
  livreurId?: number | null;
  livreurNom?: string | null;
  livreurPrenom?: string | null;
  livreurEmail?: string | null;
  livreurAdresse?: string | null;
  cooperatives?: ICooperative[] | null;
}

export class Livreur implements ILivreur {
  constructor(
    public id?: number,
    public livreurId?: number | null,
    public livreurNom?: string | null,
    public livreurPrenom?: string | null,
    public livreurEmail?: string | null,
    public livreurAdresse?: string | null,
    public cooperatives?: ICooperative[] | null
  ) {}
}

export function getLivreurIdentifier(livreur: ILivreur): number | undefined {
  return livreur.id;
}
