# AndroidOblig - Magic Stuff

## Funksjonell beskrivelse
**Magic Stuff** er en applikasjon for samlekortspillet [Magic The Gathering](https://magic.wizards.com/en).
Applikasjonen er laget ifm en obligatorisk innlevering i Applikasjonsutvikling for mobile enheter.

Applikasjonen skal fasilitere søk på kort mot API'et [magicthegathering.io](https://docs.magicthegathering.io/),
der kort man har søkt på blir lagret i en søkshistorikk.
I denne søkshistorikken skal det være mulig å 'pinne' kort av interesse,
slik at disse blir lagret. **Magic Stuff** har også 2 tellere som representerer
spillernes hitpoints gjennom en duell.


Applikasjonens komponenter kan grovt deles inn i 3 deler:
* Magic Life Counter
* Søk på kort
* Søkshistorikk og lagrede kort

### Magic Life Counter
Denne delen av Applikasjonen gjør følgende:
* To hitpoints tellere som representerer en spillers liv
* Fasiliterer login og logout for bruker
* Meny for navigering i applikasjonen
* Sender intent med søkestreng til SearchActivity

### Søk på kort
Denne delen av Applikasjonen gjør følgende:
* Utfører søk på kort. Ved flere treff, skal flere resultater vises.
* Ved trykk på kort, vises detaljert informasjon om kortet. Herfra kan man lagre kort og notat om kortet dersom man er innlogget
* Inneholder søkerbar for søk innad i aktiviteten

### Søkshistorikk og lagrede søk
Vises kun dersom bruker er innlogget
Denne delen av Applikasjonen gjør følgende:
* Henter søkshistorikk og viser en liste av alle tidligere søk
* Ved trykk på tidligere søk, utføres nytt søket på ny i Søk-aktiviteten
* Henter lagrede kort og notater man har lagt inn om kortet, og mulighet for å oppdatere notatet
* Ved trykk på kort, vises detaljert informasjon om kortet

## Forklaring - GUI
### Magic Life Counter
Startskjerm: Inneholder to teller med mulighet for inkrementering og dekrementering.
I midten av skjermen er en Floating Action Button. Ved trykk på denne, vises en ekspanderende meny.
Denne menyen inneholder 4 elementer.
* Søk
* Navigasjon til korthistorikk
* Knapp for terninkast (1-6)
* Åpne et fragment som fasiliterer innlogging av en spesifikk bruker.

Man kan ikke navigere til søkshistorikk uten å være innlogget

### Søk på kort
Denne delen av applikasjonen kan nås på 2 forskjellige måter:
* Fra søkebar i menyen til Startskjerm.
* Ved trykk på kort i Søkshistorikk og lagrede søk

Består i hovedsak av:
* SearchView
* RecyclerView med tilknyttet kort-adapter

Treff på kort i magicthegathering.io API'et skal populere et Array. RecyclerView'et
viser søkeresultatene i CardViews. Trykk på bildet av kortet skal ekspandere bildet.
Andre trykk som registreres i CardViewet, åpner et fragment der all relevant informasjon
om kortet vises. (Ikke implementert)

### Søkshistorikk og lagrede søk
Denne delen av applikasjonen består i hovedsak av:
* TabLayout med to Tabs
* ViewPager
* 2 fragmenter: **PinnedCardsFragment** og **RecentCardsFragment**

#### TabLayout
* **Tab 1**
Viser PinnedCardsFragment. Swipe-left sletter et lagret kort. Swipe-høyre bytter til RecentSearchesFragment.
* **Tab 2**
Viser RecentSearchesFragment. Swipe-left bytter til PinnedCardsFragment
#### **ViewPager**
Brukes for å bytte mellom fragmentene. Det fungerer også å swipe mellom fragmentene.

#### Fragmentene
Felles for begge fragmentene er at de har et RecyclerView. Hvert kort består av
et CardView. De har en ItemTouchHelper der swipe LEFT/RIGHT sletter. Drag UP/DOWN
endre posisjon i adapter (skal også oppdatere position i databasen).
Ved klikk på kortene skal det gjøres et søk på korttittel og vises i Søk på kort.

* **PinnedCardsFragment**
Har et RecyclerView som viser lagrede kort.
* **RecentCardsFragment**
Har et RecyclerView som viser en søkshistorikk.

## TODO
### Generelt
* Implementere Up-nagivation. [Up-navigation](https://developer.android.com/design/patterns/navigation.html)
