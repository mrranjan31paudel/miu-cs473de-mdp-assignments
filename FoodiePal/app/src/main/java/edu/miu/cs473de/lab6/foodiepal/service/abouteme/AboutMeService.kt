package edu.miu.cs473de.lab6.foodiepal.service.abouteme

import edu.miu.cs473de.lab6.foodiepal.data.aboutme.AboutMeItem
import edu.miu.cs473de.lab6.foodiepal.errors.ValidationException
import edu.miu.cs473de.lab6.foodiepal.service.DatabaseService
import kotlinx.coroutines.runBlocking

class AboutMeService {

    companion object {
        private fun validateNewAboutMeItem(
            title: String,
            description: String
        ) {
            if (title.isEmpty() || title.length < 5 || title.length > 100) {
                throw ValidationException("aboutMeItemTitle", "Required with at least 5 characters!")
            }
            if (description.isEmpty() || description.length < 10 || description.length > 1000) {
                throw ValidationException("aboutMeItemDescription", "Required with at least 10 characters!")
            }
        }

        fun createAboutMeItem(title: String, description: String): Int {
            validateNewAboutMeItem(title, description)

            val aboutMeDao = DatabaseService.db?.aboutMeDao()
            val ids = runBlocking {
                return@runBlocking aboutMeDao?.addItems(AboutMeItem(0, title, description))
            }

            if (ids.isNullOrEmpty()) return 0

            return ids[0].toInt()
        }

        fun insertBulkItems(items: ArrayList<AboutMeItem>) {
            try {
                for (item in items) {
                    validateNewAboutMeItem(item.title, item.description)
                }
            }
            catch (e: ValidationException) {
                println("ERRORs: validation error $e")
                throw Exception("Something wrong with the data")
            }

            val aboutMeDao = DatabaseService.db?.aboutMeDao()
            runBlocking {
                aboutMeDao?.addItems(*(items.toTypedArray()))
            }
        }

        fun getAllItems(): ArrayList<AboutMeItem> {
            val aboutMeDao = DatabaseService.db?.aboutMeDao()
            val items = runBlocking {
                aboutMeDao?.getAll()
            }

            return (items ?: listOf()) as ArrayList
        }

        fun getItemById(id: Int): AboutMeItem? {
            val aboutMeDao = DatabaseService.db?.aboutMeDao()
            return runBlocking {
                return@runBlocking aboutMeDao?.getById(id)
            }
        }
    }
}