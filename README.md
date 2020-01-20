[![CircleCI](https://circleci.com/gh/navikt/dittnav-api.svg?style=svg&circle-token=999f4ceaae1ed22eb2272f9b6a4c5d6b9892d119)](https://circleci.com/gh/navikt/dittnav-api)

# DittNAV api

Proxy mellom dittnav-frontend og dittnav-legacy-api/dittnav-event-handler

# Kom i gang
1. Bygg dittnav-api ved å kjøre `gradle build`
2. Start alle appens avhengigheter ved å kjøre `docker-compose -d`
3. Ta ned dittnav-api ved å kjøre `docker-compose kill api`
4. Start appen lokalt ved å kjøre `./gradlew runServer`
5. Appen nås på ´http://localhost:8091´

# Feilsøking
For å være sikker på at man får en ny tom database og tomme kafka-topics kan man kjøre kommandoen: `docker-compose down -v`

# Henvendelser

Spørsmål knyttet til koden eller prosjektet kan rettes mot https://github.com/orgs/navikt/teams/personbruker

## For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen #team-personbruker.
