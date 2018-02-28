# AndroidOblig - Magic stuff

## Funksjonell beskrivelse
**Magic Stuff** er en applikasjon for samlekortspillet [Magic The Gathering](https://magic.wizards.com/en).
Applikasjonen er laget ifm en obligatorisk innlevering i Applikasjonsutvikling for mobile enheter.

Applikasjonen skal fasilitere søk på kort mot API'et [MTGAPI](https://docs.magicthegathering.io/),
der kort man har søkt på blir lagret i en søkshistorikk.
I denne søkshistorikken skal det være mulig å 'pinne' kort av interesse,
slik at disse blir lagret. **Magic stuff** har også en 2 tellere som representerer
spillernes hitpoints gjennom en duell.  


Applikasjonens komponenter kan grovt deles inn i 3 deler:
* Magic Life Counter
* Søk på kort
* Søkshistorikk og lagrede søk

### Magic Life Counter
Denne delen av Applikasjonen gjør følgende:
* To hitpoints tellere som representerer en spillers liv
* Fasiliterer login for bruker
* Meny for navigering nedover et nivå i applikasjonen
* Sender intent for å søke på kort

### Søk på kort
Denne delen av Applikasjonen gjør følgende:
* Utfører søk på kort. Ved flere treff, skal flere resultater vises.
* Ved trykk på treff, vises detaljert informasjon om kortet

### Søkshistorikk og lagrede søk
Denne delen av Applikasjonen gjør følgende:
* Henter søkshistorikk og viser en preview av alle tidligere søk
* Lagrer søk av interesse

## Forklaring - GUI
