# Atelier Module 1 : Transformation d'un Monolithe

## Contexte

Vous êtes consultant pour **ShopEasy**, une entreprise de e-commerce dont l'application monolithique commence à montrer ses limites :

- Déploiements risqués (tout ou rien)
- Équipes qui se marchent sur les pieds
- Impossible de scaler le module paiement indépendamment
- Temps de build : 45 minutes

## Objectifs de l'atelier

1. Analyser l'architecture actuelle
2. Identifier les bounded contexts
3. Proposer un découpage en microservices
4. Schématiser l'architecture cible

---

## Partie 1 : Analyse du monolithe 
### Architecture actuelle

```
shopeasy-monolith/
├── src/main/java/com/shopeasy/
│   ├── user/
│   │   ├── User.java
│   │   ├── UserRepository.java
│   │   ├── UserService.java
│   │   └── UserController.java
│   ├── product/
│   │   ├── Product.java
│   │   ├── Category.java
│   │   ├── ProductRepository.java
│   │   ├── ProductService.java
│   │   └── ProductController.java
│   ├── cart/
│   │   ├── Cart.java
│   │   ├── CartItem.java
│   │   ├── CartService.java
│   │   └── CartController.java
│   ├── order/
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   ├── OrderRepository.java
│   │   ├── OrderService.java
│   │   └── OrderController.java
│   ├── payment/
│   │   ├── Payment.java
│   │   ├── PaymentGateway.java
│   │   ├── PaymentService.java
│   │   └── PaymentController.java
│   ├── shipping/
│   │   ├── Shipment.java
│   │   ├── ShippingService.java
│   │   └── ShippingController.java
│   ├── inventory/
│   │   ├── Stock.java
│   │   ├── InventoryService.java
│   │   └── InventoryController.java
│   └── notification/
│       ├── EmailService.java
│       └── SmsService.java
└── src/main/resources/
    └── application.properties (connexion à une seule BDD)
```

### Questions d'analyse

1. **Identifiez les dépendances** : Quels modules dépendent de quels autres ?

2. **Identifiez les couplages forts** : Où voyez-vous des problèmes potentiels ?

3. **Analysez les données** : Quelles entités sont partagées entre modules ?

---

## Partie 2 : Identification des Bounded Contexts 

### Travail à réaliser

Identifiez les bounded contexts en répondant aux questions :

| Module | Responsabilité métier | Données propres | Équipe potentielle |
|--------|----------------------|-----------------|-------------------|
| User | | | |
| Product | | | |
| Cart | | | |
| Order | | | |
| Payment | | | |
| Shipping | | | |
| Inventory | | | |
| Notification | | | |

### Questions de réflexion

1. Le module **Cart** doit-il être un service séparé ou intégré à **Order** ?

2. **Inventory** et **Product** sont-ils un seul bounded context ?

3. **Notification** est-il un service métier ou technique ?

---

## Partie 3 : Proposition de découpage 

### Schéma à compléter

Dessinez l'architecture cible avec :

- [ ] Les microservices identifiés
- [ ] Les bases de données par service
- [ ] Les communications (synchrones/asynchrones)
- [ ] L'API Gateway
- [ ] Les files de messages si nécessaire

### Template de découpage

```
┌─────────────────────────────────────────────────────────────┐
│                        API Gateway                          │
└─────────────────────────────────────────────────────────────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
        ▼                     ▼                     ▼
┌───────────────┐   ┌───────────────┐   ┌───────────────┐
│               │   │               │   │               │
│  Service 1    │   │  Service 2    │   │  Service 3    │
│               │   │               │   │               │
└───────┬───────┘   └───────┬───────┘   └───────┬───────┘
        │                   │                   │
        ▼                   ▼                   ▼
    ┌───────┐           ┌───────┐           ┌───────┐
    │ DB 1  │           │ DB 2  │           │ DB 3  │
    └───────┘           └───────┘           └───────┘
```

### Décisions à prendre

Pour chaque service identifié, précisez :

| Service | Base de données | Protocole communication | Justification |
|---------|-----------------|------------------------|---------------|
| | | | |

---

## Partie 4 : Plan de migration 

### Ordre de migration

Proposez un ordre de migration pragmatique :

1. **Phase 1** : Service(s) le(s) plus indépendant(s)
   - Quel(s) service(s) ?
   - Pourquoi ?

2. **Phase 2** : Service(s) à forte valeur ajoutée
   - Quel(s) service(s) ?
   - Pourquoi ?

3. **Phase 3** : Services restants
   - ...

### Pattern Strangler Fig

Comment appliquer le pattern Strangler Fig pour migrer progressivement ?

```
Étape 1: [Votre réponse]
Étape 2: [Votre réponse]
Étape 3: [Votre réponse]
```

---

## Livrables attendus

1. **Schéma** de l'architecture cible
2. **Tableau** des microservices avec leurs responsabilités
3. **Plan** de migration en phases


