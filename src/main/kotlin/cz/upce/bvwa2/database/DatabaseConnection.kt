package cz.upce.bvwa2.database

import cz.upce.bvwa2.Config
import cz.upce.bvwa2.database.dao.IUserDao
import cz.upce.bvwa2.database.model.User
import cz.upce.bvwa2.database.table.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.transaction

// Třída DatabaseConnection zajišťuje připojení k databázi a její inicializaci.
class DatabaseConnection(
    // Konfigurační objekt obsahující nastavení pro připojení k databázi.
    private val config: Config,
    // DAO pro manipulaci s uživateli.
    private val userDao: IUserDao
) {

    // Připojení k databázi.
    fun connect() {
        // Připojení k databázi pomocí řetězce z konfigurace.
        Database.connect(config.database.connectionString)
    }

    // Inicializace databáze - vytvoření tabulek a vložení základních dat.
    fun init() {
        transaction {
            // Vytvoření databázových tabulek.
            SchemaUtils.create(Messages)
            SchemaUtils.create(Users)
            SchemaUtils.create(Roles)
            SchemaUtils.create(Sessions)
            SchemaUtils.create(Genders)

            // Vložení základních rolí do tabulky Roles.
            Roles.insertIgnore {
                it[roleName] = "admin"
            }
            Roles.insertIgnore {
                it[roleName] = "user"
            }

            // Vložení pohlaví do tabulky Genders.
            Genders.insertIgnore {
                it[genderName] = "Muž"
            }
            Genders.insertIgnore {
                it[genderName] = "Žena"
            }

            // Pokus o vytvoření základních uživatelských účtů.
            runCatching {
                userDao.add(User(
                    "admin",
                    "admin",
                    "admin",
                    null,
                    "admin",
                    "admin",
                    "admin",
                    "Muž",
                    "admin"
                ))
            }
            runCatching {
                userDao.add(User(
                    "user",
                    "user",
                    "user",
                    null,
                    "user",
                    "user",
                    "user",
                    "Muž",
                    "user"
                ))
            }
        }
    }
}