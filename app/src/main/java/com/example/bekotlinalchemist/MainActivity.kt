package com.example.bekotlinalchemist

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast

import com.example.bekotlinalchemist.pojo.Pocion
import com.example.bekotlinalchemist.pojo.TiposPociones
import com.example.bekotlinalchemist.utils.GetDaJSON
import com.example.bekotlinalchemist.utils.RestClient
import com.loopj.android.http.JsonHttpResponseHandler

import org.json.JSONObject

import java.util.Random

import cz.msebera.android.httpclient.Header

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var JSONGetter: GetDaJSON? = null

    private var tiposPociones: TiposPociones? = null

    private var randomGenerator: Random? = null

    private var txtV_Resultados: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        JSONGetter = GetDaJSON(this@MainActivity)

        tiposPociones = TiposPociones()
        descarga("https://api.myjson.com/bins/167a4n")

        randomGenerator = Random()

        txtV_Resultados = findViewById<View>(R.id.txtv_Results) as TextView
    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_share) {

        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_aboutus) {

        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun onClick_genNewPotion(v: View) {
        when (v.id) {
            R.id.btn_genPotion -> genNewPotion()
        }
    }

    private fun genNewPotion() {
        val pocion = Pocion(
                tiposPociones!!.getoYs()[randomGenerator!!.nextInt(tiposPociones!!.getoYs().size)],
                tiposPociones!!.getoYs()[randomGenerator!!.nextInt(tiposPociones!!.getoYs().size)],
                tiposPociones!!.etiquetas[randomGenerator!!.nextInt(tiposPociones!!.etiquetas.size)],
                tiposPociones!!.efectosN[randomGenerator!!.nextInt(tiposPociones!!.efectosN.size)],
                tiposPociones!!.efectosP[randomGenerator!!.nextInt(tiposPociones!!.efectosP.size)]
        )

        txtV_Resultados!!.text = pocion.toString()
    }

    private fun descarga(web: String) {
        val progreso = ProgressDialog(this)
        RestClient.get(web, object : JsonHttpResponseHandler() {
            override fun onStart() {
                super.onStart()
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                progreso.setMessage("Conectando...")
                progreso.setCancelable(true)
                progreso.show()
            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                try {
                    progreso.dismiss()
                    tiposPociones = JSONGetter!!.getParamsPociones(response)
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "¡Error al obtener parámetros! :(",
                            Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONObject?) {
                progreso.dismiss()
                Toast.makeText(this@MainActivity, "¡Ha fallado la descarga! :(",
                        Toast.LENGTH_SHORT).show()
            }
        })
    }
}
