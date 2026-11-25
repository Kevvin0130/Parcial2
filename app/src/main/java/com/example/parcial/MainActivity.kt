
package com.example.parcial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.parcial.ui.theme.ParcialTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ParcialTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(
                            text = "Mis viajes",
                            style = MaterialTheme.typography.headlineLarge
                        )

                        Text(
                            text = "Bienvenido a TravelExpenses",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = "Europa 2026",
                                    style = MaterialTheme.typography.titleLarge
                                )

                                Text("Destino: Italia")
                                Text("10/06/2026 - 20/06/2026")

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        // Próximamente: abrir detalle
                                    }
                                ) {
                                    Text("Ver viaje")
                                }
                            }
                        }

                        Button(
                            onClick = {
                                // Próximamente: crear viaje
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("+ Crear viaje")
                        }
                    }
                }
            }
        }
    }
}
