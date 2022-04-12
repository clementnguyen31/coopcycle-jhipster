import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Recherche e2e test', () => {
  const recherchePageUrl = '/recherche';
  const recherchePageUrlPattern = new RegExp('/recherche(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const rechercheSample = {};

  let recherche: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/recherches+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/recherches').as('postEntityRequest');
    cy.intercept('DELETE', '/api/recherches/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (recherche) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/recherches/${recherche.id}`,
      }).then(() => {
        recherche = undefined;
      });
    }
  });

  it('Recherches menu should load Recherches page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('recherche');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Recherche').should('exist');
    cy.url().should('match', recherchePageUrlPattern);
  });

  describe('Recherche page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(recherchePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Recherche page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/recherche/new$'));
        cy.getEntityCreateUpdateHeading('Recherche');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', recherchePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/recherches',
          body: rechercheSample,
        }).then(({ body }) => {
          recherche = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/recherches+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [recherche],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(recherchePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Recherche page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('recherche');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', recherchePageUrlPattern);
      });

      it('edit button click should load edit Recherche page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Recherche');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', recherchePageUrlPattern);
      });

      it('last delete button click should delete instance of Recherche', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('recherche').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', recherchePageUrlPattern);

        recherche = undefined;
      });
    });
  });

  describe('new Recherche page', () => {
    beforeEach(() => {
      cy.visit(`${recherchePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Recherche');
    });

    it('should create an instance of Recherche', () => {
      cy.get(`[data-cy="rechercheId"]`).type('31847').should('have.value', '31847');

      cy.get(`[data-cy="rechercheThematique"]`).type('Concrete ivory').should('have.value', 'Concrete ivory');

      cy.get(`[data-cy="rechercheGeographique"]`).type('Borders').should('have.value', 'Borders');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        recherche = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', recherchePageUrlPattern);
    });
  });
});
