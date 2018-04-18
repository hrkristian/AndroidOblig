# AndroidOblig - Magic Stuff

## Funksjonell beskrivelse
**Magic Stuff** er en applikasjon for samlekortspillet [Magic The Gathering](https://magic.wizards.com/en).
Applikasjonen er laget ifm en obligatorisk innlevering i Applikasjonsutvikling for mobile enheter.

Applikasjonen skal fasilitere søk på kort mot API'et [magicthegathering.io](https://docs.magicthegathering.io/),
der kort man har søkt på blir lagret i en søkshistorikk. Fra søkeresultatene kan man lagre kort og notat om kortet.
**Magic Stuff** har også 2 tellere som representerer hitpoints gjennom en duell.


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
* Utfører søk på kortnavn. Ved flere treff, skal flere resultater vises.
* Ved trykk på kort, vises detaljert informasjon om kortet. Herfra kan man lagre kort og notat om kortet dersom man er innlogget
* Inneholder søkebar for søk, gjør nytt søk og oppdaterer aktiviteten.

### Søkshistorikk og lagrede kort
Vises kun dersom bruker er innlogget
Denne delen av Applikasjonen gjør følgende:
* Henter søkshistorikk og viser en liste av alle tidligere søk. Søk blir lagret og hentet fra lokal database på telefon.
* Ved trykk på tidligere søk, utføres nytt søket på ny i Søk-aktiviteten
* Henter lagrede kort og notater man har lagt inn om kortet, og mulighet for å oppdatere notatet
* Ved trykk på kort, vises detaljert informasjon om kortet

## Forklaring - GUI
### Magic Life Counter
Startskjerm: Inneholder to tellere med mulighet for inkrementering og dekrementering.
I midten av skjermen er en Floating Action Button. Ved trykk på denne, vises en ekspanderende meny.
Denne menyen inneholder 4 elementer.
* Søk
* Navigasjon til korthistorikk
* Knapp for terninkast (1-6)
* Åpne et fragment som fasiliterer innlogging/utlogging av en spesifikk bruker.

Man kan ikke navigere til søkshistorikk uten å være innlogget

### Søk på kort
Denne delen av applikasjonen kan nås på 2 forskjellige måter:
* Fra søkebar i menyen til Startskjerm.
* Ved trykk på element i søkshistorikk

Består i hovedsak av:
* SearchView
* RecyclerView med tilknyttet kort-adapter

Treff på kort i magicthegathering.io API'et populerer et Array. RecyclerView'et
viser søkeresultatene i CardViews. Trykk på bildet på kortet skal åpner et fragment der all relevant informasjon
om kortet vises.

### Søkshistorikk og lagrede søk
Denne delen av applikasjonen består i hovedsak av:
* TabLayout med to Tabs
* ViewPager
* 2 fragmenter: **PinnedCardsFragment** og **RecentSearchesFragment**

#### TabLayout
* **Tab 1**
Viser PinnedCardsFragment. Swipe-left sletter et lagret kort. Swipe-høyre bytter til RecentSearchesFragment.
* **Tab 2**
Viser RecentSearchesFragment. Swipe-left bytter til PinnedCardsFragment
#### **ViewPager**
Brukes for å bytte mellom fragmentene. Det fungerer også å swipe mellom fragmentene.

#### Fragmentene
Felles for begge fragmentene er at de har et RecyclerView. Hvert kort består av
et CardView.
Ved klikk på kortene skal det gjøres et søk på korttittel og vises i Søk på kort.

* **PinnedCardsFragment**
Har et RecyclerView som viser lagrede kort.
* **RecentCardsFragment**
Har et RecyclerView som viser en søkshistorikk.

### Til den som evaluerer
Login-info:
* Brukernavn: kvisli
* Passord: heihei

Kortnavn du kan søke på, dersom du ikke er kjent med spillet:
* monastery swiftspear
* birds of paradise
* mountain
* counterspell
* negate
* ponyback brigade
* goblin guide
