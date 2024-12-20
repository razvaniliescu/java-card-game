

# Tema POO  - GwentStone

## Structura proiectului

* cards/
  * Card: aici am datele generale pentru toate elementele de pe tabla de joc. Din aceasta clasa sunt extinse clasele de minioni si eroi. Pe langa campurile mentionate in enunt, am introdus si un camp hasAttackedThisTurn pentru a verifica daca o carte a actionat in tura curenta. Aici am si metoda care afiseaza cartea in format JSON
  * Minion: clasa pentru minioni. Pe langa variabilele mentionate in enunt, eu am mai introdus si campurile frozen pentru a verifica daca un minion e inghetat si frontRow pentru a verifica cum trebuie pozitionat minionul. In aceasta clasa am si metoda de atac
  * Tank: clasa pentru minionii cu abilitatea pasiva de tanc. Diferenta fata de un minion normal e ca ei au campul de frontRow setat ca true.
  * Special minion: clasa pentru minionii cu abilitati. Aici am metoda de abilitate pe care o suprascriu in subclasa specifica fiecarui minion special.
  * Hero: clasa pentru eroi. Clasa contine metoda goala de abilitate pe care o suprascriu in subclasa specifica fiecarui erou
* deck/
  * Deck: clasa contine o lista de minioni si un numar intreg pentru numarul de carti. Am considerat atat pachetul, cat si mana jucatorului, un obiect de tipul deck. Aici am metode pentru adaugarea si eliminarea cartilor, si o metoda pentru afisarea intregului deck in format JSON.
  * DeckList: clasa ce contine o lista de deck-uri, numarul de deck-uri si numarul de carti din fiecare deck, folosita pentru a prelua mai usor datele din input.
* game/
  * Player: fiecare jucator are un index, mana curenta, mana primita pe runda, lista de deck-uri, deck-ul curent, mana (hand), erou, cat si numarul de jocuri castigate si jucate. Aici am metode pentru tragerea cartilor, punerea minionilor pe tabla si actualizarea statisticilor jucatorilor.
  * Game: clasa contine cei doi jucatori, tabla, mana pe care jucatorii o primesc la inceputul rundei si numarul de ture din acea runda (pentru a tine cont de cand incepe o noua runda). Aici sunt procesate toate comenzile pe care le preiau din input (fara cele de debug), iar erorile sunt trimise mai departe in clasele de mai jos pentru a fi afisate.
* output/
  * Output: contine doar elementele de baza pentru a afisa ceva in format JSON: un objectNode, un objectMapper si un arrayNode (output-ul final). Clasele de mai jos mostenesc aceasta clasa.
  * Error: clasa folosita pentru a afisa erorile comenzilor de la input.
  * Debug: clasa folosita pentru a afisa rezultatul comenzilor de debug. Aceasta clasa mai are in plus si un camp de game pentru a prelua mai usor informatiile necesare.
* main/
  * Main: mai intai preiau din input listele de deck-uri si initializez cei 2 jucatori, dupa care, pentru fiecare joc, setez eroul si deck-ul fiecarui jucator, iar apoi parcurg toate comenzile folosind un switch mare, punand intr-un arrayNode rezultatul fiecarei instructiuni. La final, scriu continutul arrayNode-ului in fisierul de output.
* constants/
  * Constants: clasa ce contine constantele folosite pe parcursul temei (coduri de eroare, pentru randurile tablei sau pentru viata initiala a eroilor)