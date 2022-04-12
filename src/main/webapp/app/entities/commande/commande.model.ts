import { IClient } from 'app/entities/client/client.model';
import { IRestaurateur } from 'app/entities/restaurateur/restaurateur.model';

export interface ICommande {
  id?: number;
  commandeId?: number | null;
  commandeLieu?: string | null;
  commandeEcheance?: string | null;
  commandeLibelle?: string | null;
  clients?: IClient[] | null;
  restaurateurs?: IRestaurateur[] | null;
}

export class Commande implements ICommande {
  constructor(
    public id?: number,
    public commandeId?: number | null,
    public commandeLieu?: string | null,
    public commandeEcheance?: string | null,
    public commandeLibelle?: string | null,
    public clients?: IClient[] | null,
    public restaurateurs?: IRestaurateur[] | null
  ) {}
}

export function getCommandeIdentifier(commande: ICommande): number | undefined {
  return commande.id;
}
