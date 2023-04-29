package com.example.mensaesswerkmenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.extractIt
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.div

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FetchData()
    }

    data class MensaItem(var name: String = "DefaultName", var price: String = "0.0")
    data class MensaMenu(var date: String = "", var items: MutableList<MensaItem> = mutableListOf())
    fun FetchData() {
        val site = skrape(HttpFetcher) {
            request {
                url = "https://www.swffm.de/essen-trinken/speiseplaene/mensa-esswerk"
            }

            extractIt<MensaMenu> { results ->
                htmlDocument {
                    val menu = findFirst(".speiseplan") {
                        findFirst(".panel-heading"){
                            results.date = text
                        }

                        findAll("tr"){
                            this.forEach {
                                var item = MensaItem()

                                it.findFirst(".menu_name") {
                                    item.name = this.text;
                                }

                                it.findFirst(".col-md-2") {
                                    findFirst("strong") {
                                        item.price = this.text
                                    }
                                }

                                results.items.add(item)
                            }
                        }

                    }
                }

                Log.d("Scraper", "Done.")
            }
        }
    }

}