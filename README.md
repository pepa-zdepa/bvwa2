Cílem praktického výstupu týmu je navrhnout a vyvinout na straně frontendu (JavaScript/HTML/CSS) i backendu (PHP) jednoduchý informační systém, který bude disponovat následující funkcionalitou (+ zdrojové kódy aplikace budou dostatečně komentované, v případě přejmutí částí kódů tyto ozdrojované a celá aplikace bude disponovat základním manuálem):
 

### frontend:
Umožní zobrazit formulář pro přihlašování a registraci uživatele (vyžaduje jméno, příjmení, email, telefon, pohlaví, profilová fotografie, login, heslo).
Jednotlivé vstupy budou s adekvátním typem HTML INPUT (telephone, file, email, atd.) a u každého z nich se ro  vnou na straně frontendu bude kontrolovat validita vstupu (formát emailu, telefonu, vyplněné položky...). Uživatelské rozhraní bude responzivní pro mobilní/desktopové zobrazení.


### backend:
Registrace uživatele zapíše data o něj do databáze - citlivé údaje dle GDPR buďto v hashované (heslo) či šifrované podobě (údaje které budete ješte pořebovat číst) a to adekvátně bezpečným algoritmem.
Při procesu registrace se opět i na straně serveru validují veškeré vstupy o uživatele budťo dostupnými funkcemi v PHP/frameworku či pomocí regulárních výrazů. Taktéž musí být registrovaný uživatel unikátní (login). Při SQL je třeba dbát na bezpečnost vstupů a vyvarovat se například útokům SQL injection. Příchozí obrázek se uloží na serveru buď v databázi či příslušném adresáři a to ve formátu JPEG (kvalita 90) v jednotném proporcionálním rozlišení 800 x XXX bodů. Pokud bude zaslaný formát či rolišení jiné, nebude akceptováno. Do formátu JPEG budou převedeny příchozí obrázky v libovolném z běžných formátů (PNG, GIF, BMP, TIFF). V systému bude již registrovaný výchozí uživatel který bude mít roli "admin" a bude moci po svém přihlášení prohlížet a editovat libovolné údaje jiných uživatelů a taktéž případně jiným uživatelům přidělit roli admina. Jinak bude moci editovat se údaje každý uživatel pouze sám sobě.
 

Přihlášení bude využívat session, po 20 minutách nečinnosti bude uživatel automaticky odhlášen. Při přihlášení je třeba dbát na bezpečnost vstupů a vyvarovat se například útokům session hijacking. Uživateli se po přihlášení zbrazí jeho osobní údaje, jeho role v systému a možnost interního komunikačního systému s ostatními uživateli (podobné jako klasické emaily) - odesílané zprávy se uchovají ve složce odeslaných zpráv, v případě nové příchozí zprávy se zobrazí uživateli notifikace. Zprávy se na serveru budou rovněž v databázi uchovávat v šifrované podobě.
