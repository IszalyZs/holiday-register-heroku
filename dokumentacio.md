# Munkaidő nyilvántartó program

A program a munkatársak szabadságainak kezelésére készült.  

Elvárt működés:

* a munkatárshoz fel kell tudni venni, hogy mettől meddig van szabadságon a kezdő és a végdátum megadásával
* lehessen törölni a szabadságot a kezdő és a végdátum megadásával
* hibás intervallumok kiszűrése

Le kell tudni kérdezni a következőket:

* egy munkatárs egy adott időszakban (dátumtól dátumig) kivett összes szabadság intervallumát  
* egy munkatárs egy adott időszakban (dátumtól dátumig) hány napot dolgozott  
* egy munkatárs egy adott év adott hónapjában hány napot dolgozott
 
A program, amely egy Spring Boot alapú REST API, Java nyelven készült, Ultimate IntelliJ programmal, Linux környezetben. Maven project dockerizált környezetben. A következő dependenciákat tartalmazza:  

* H2  
* Flyway  
* Lombok  
* Swagger  
* JPA  
* ModelMapper

A program indítása: docker-compose up, leállítás: docker-compose down  
Indítás után lefuttatja az összes tesztet. A port 8888-ra van állítva, a (http://localhost:8888/swagger-ui.html) oldalon tesztelhető a program, vannak kezdő adatok is. Az api dokumentáció itt érhető el: (http://localhost:8888/api-docs)

## Részletes ismertető:

Négy adatmodellt tartalmaz:  

* Employee  
* Holiday  
* Children  
* HolidayDay

Mindegyikhez készült endpoint a CRUD műveletekhez. A HolidayController-ben lettek megvalósítva az előírt feladatok. A program egy employee felvitelével kezdődik, minden esetben DTO-kat használtam, amely a ModelMapper segítségével lettek átkonvertálva a mentéshez szükséges objektumokra.  

### Employee részletezése:

Az összes entitásnál van validáció, hibás vagy hiányzó adatoknál hiba üzenetet add vissza. A következő adatokat kötelező kitölteni:  

* firstName  
* lastName  
* identityNumber  
* birthDate  
* workplace  
* position  
* dateOfEntry  

Van még egy **beginningOfEmployment** field is, ezt csak a tényleges munkába állásnál kell kitölteni, ez lehet null is az adott időpontban. Az összes lekérdezés és a szabadság felvitele ettől az időponttól kezdődik, null esetén hibát dob. Ha a lekérdezés kezdő dátuma korábbi, akkor a lekérdezés kezdő dátuma felülíródik a beginningOfEmployment dátumával. A szabadság felvitelénél sem lehet a kezdő dátum korábbi, mint a beginningOfEmployment, itt is hibát dob. Az intervallum megadásánál szintén figyeli az elvárt követelményeket, sok hibakezelés lett beépítve. A kezdő szabadság az életkor beírása után automatikusan kiszámításra kerül. Figyelve lett még a dateOfEntry dátuma is a beginningOfDate-hez viszonyítva, itt viszont a dateOfEntry nem lehet később.  
Az identityNumber egy 9 hosszúságú egyedi azonosítószám. Ha minden adat valid, akkor az objektum elmentése megtörténik. Mentésre kerülnek még a következő mezők is:  

* basicLeave  
* extraLeave  
* sumHoliday  
* nextYearLeave  
* sumHolidayNextYear 

A sumHoliday és a sumHolidayNextYear az összes idei és a következő évi kivett szabadságok számát tartalmazza. A program úgy lett megírva, hogy szabadságot csak a tárgyévben vagy a tárgyévtől a következő évre megjelölt folyamatos időszakra lehessen kivenni, természetesen ha van elegendő még kivehető szabadság. Tárgyévtől korábbi vagy csak a következő évre megjelölt időszak esetén hibát dob. Az extra szabadság a gyermekek számától függ,(1 esetén plusz 2 nap, 2 esetén plusz 4 nap , 3 vagy több esetén plusz 7 nap). A program automatikusan hozzáadja a children felvitele után. Fontos megemlítenem, mivel nem találtam egységes szabályzatot arra vonatkozóan, hogy a szabadság átvihető-e a következő évre, így az tárgyévi szabadság csak a tárgyévre, a következő évi szabadság csak a következő évre vonatkozik.

endpointok listája:  

- /employee/all  
- /employee/{id}/delete  
- /employee/{id}/get  
- /employee/add  
- /employee/{id}/update


### Children részletezése:

A következő adatokat kötelező kitölteni:  

* firstName  
* lastName  
* birthDay

A felvitel esetén meg kell adni még az employee id-t is. A birthDay esetén a dátumnak korábbinak kell lenni az aznapi dátumnál.  

endpointok listája:  

- /children/all  
- /children/{id}/delete  
- /children/{id}/get  
- /children/employee/{id}/add  
- /children/{id}/update

### HolidayDay részletezése:

A következő adatokat kötelező kitölteni:

* year  
* localDate  
 
A year egyedi azonosító. Lekérdezéseknél fontos, hogy legyen az adott évhez megfelelő ünnepnap adatbázis. Ha olyan évet szeretnénk listázni, amelyhez nincs meg a megfelelő ünnepnap adatbázis, akkor hibát dob, a hiányzó év megjelölésével. Az éveket "yyyy-mm-dd" formátumban kell rögzíteni. Ha az egyes időpontok évei eltérnek egymástól és az year-től, akkor hibát dob.  

endpointok listája:  

- /holidayday/all  
- /holidayday/{id}/delete  
- /holidayday/{id}/get  
- /holidayday/add  
- /holidayday/{id}/update

### Holiday részletezése:


A következő adatokat kötelező kitölteni:  
  
* startDate  
* finsihDate  

Az összes műveletnél az employee Id-t is meg kell adni. Az intervallumok megadásakor előforduló összes hiba kezelve lett. A szabadság intervalluma csak akkor kerül mentésre, ha elegendő a rendelkezésre álló szabadság, külön figyelve az idei és a következő évi elérhető szabadságokra is. A szabadság mentésekor az employee adatbázisban is felülírásra kerülnek a megfelelő mezők. A szabadságoknál figyelve vannak az ünnepnapok és a hétvégék is.  
A delete műveletnél a teljesen egyező intervallum törlése után a törölt szabadságok vissza állításra kerülnek az összes még elérhető szabadságokhoz.  
Az intervallumok alapján történő lekéréseknél a paraméterként kapott startyear-endyear vagy a year-month string paraméterek egy holiday objektummá lesznek átalakítva, a további műveletek már a holidayDTO-val történnek.  
A munkanapok lekérésekor egy string üzenetben láthatjuk a lekért adatot az intervallum feltüntetésével.  

A szabadságok lekérésekor két lehetőség áll rendelkezésre:  

* egy listát kapunk, amelyben időrendben felsorolva láthatjuk az adatokat  
* a szabadságok számát egy string üzenetben kapjuk meg az intervallum feltüntetésével

endpointok listája: 

- /holiday/employee/{id}/add  
- /holiday/employee/{id}/delete  
- /businessdays/employee/{id}/dateinterval  
- /businessdays/employee/{id}/year-month  
- /holidays/employee/{id}/dateinterval  
- /holidays/employee/{id}/amount







