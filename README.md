```markdown
# Player Database Microservice

This microservice handles CRUD operations for player data. It provides endpoints to retrieve player information, including support for pagination, and to reload player data from a CSV file.

## Endpoints

### Get Player by ID

Retrieve player information by player ID.

Request

`GET /api/players/{playerID}`

Example

```http
GET http://localhost:8080/api/players/aardsda01
```

Response

```json
{
    "playerID" : "aardsda01",
    "birthYear" : 1981,
    "birthMonth" : 12,
    "birthDay" : 27,
    "birthCountry" : "USA",
    "birthState" : null,
    "birthCity" : null,
    "deathYear" : null,
    "deathMonth" : null,
    "deathDay" : null,
    "deathCountry" : null,
    "deathState" : null,
    "deathCity" : null,
    "nameFirst" : null,
    "nameLast" : null,
    "nameGiven" : null,
    "weight" : null,
    "height" : null,
    "bats" : null,
    "throwsHand" : null,
    "debut" : null,
    "finalGame" : null,
    "retroID" : null,
    "bbrefID" : null
}
```

### Get All Players with Pagination

Retrieve a paginated list of players.

Request

`GET /api/players?page={page}&size={size}`

Parameters

- `page`: Page number (default is `0`)
- `size`: Number of players per page (default is `10`)

Example

```http
GET http://localhost:8080/api/players?page=0&size=2
```

Response

```json
[
    {
        "playerID" : "aardsda01",
        "birthYear" : 1981,
        "birthMonth" : 12,
        "birthDay" : 27,
        "birthCountry" : "USA",
        "birthState" : null,
        "birthCity" : null,
        "deathYear" : null,
        "deathMonth" : null,
        "deathDay" : null,
        "deathCountry" : null,
        "deathState" : null,
        "deathCity" : null,
        "nameFirst" : null,
        "nameLast" : null,
        "nameGiven" : null,
        "weight" : null,
        "height" : null,
        "bats" : null,
        "throwsHand" : null,
        "debut" : null,
        "finalGame" : null,
        "retroID" : null,
        "bbrefID" : null
    },
    {
        "playerID" : "aaronha01",
        "birthYear" : 1934,
        "birthMonth" : 2,
        "birthDay" : 5,
        "birthCountry" : "USA",
        "birthState" : null,
        "birthCity" : null,
        "deathYear" : null,
        "deathMonth" : null,
        "deathDay" : null,
        "deathCountry" : null,
        "deathState" : null,
        "deathCity" : null,
        "nameFirst" : null,
        "nameLast" : null,
        "nameGiven" : null,
        "weight" : null,
        "height" : null,
        "bats" : null,
        "throwsHand" : null,
        "debut" : null,
        "finalGame" : null,
        "retroID" : null,
        "bbrefID" : null
    }
]
```

### Reload CSV Data

Reload player data from a CSV file.

Request

`POST /api/players/reload`

Example

```http
POST http://localhost:8080/api/players/reload
```

Response

```json
{
    "status": "success",
    "message": "CSV reloaded successfully",
    "timestamp": 1724409904501
}
```

## How to Use

1. Get Player by ID: Use the specific player ID in the URL to get details of a single player.
2. Get All Players: Retrieve a list of players with pagination by providing page and size parameters.
3. Reload Data: Reload the player data from a CSV file by posting to the reload endpoint.

Make sure to replace `localhost:8080` with the actual host and port where your application is running, if different.
```