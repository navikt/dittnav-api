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

Spørsmål knyttet til koden eller prosjektet kan stilles som issues her på github.

## For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen #team-personbruker.
