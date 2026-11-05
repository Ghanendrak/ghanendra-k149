package com.dskushwahagroup.shop

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.net.URLEncoder

private const val WHATSAPP_NUMBER = "918933979022"

data class UnitInfo(val name: String, val hindi: String, val detail: String, val products: List<String>)

data class CartLine(val unit: String, val product: String, val qty: Int)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DSKApp { message -> openWhatsApp(message) } }
    }

    private fun openWhatsApp(message: String) {
        val url = "https://wa.me/$WHATSAPP_NUMBER?text=" + URLEncoder.encode(message, "UTF-8")
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (_: Exception) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$WHATSAPP_NUMBER")))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DSKApp(sendWhatsApp: (String) -> Unit) {
    // Active business units are maintained in one place. To restore a unit later,
    // simply uncomment/add its UnitInfo entry here; the horizontal menu updates automatically.
    val units = listOf(
        UnitInfo("DS PROPERTIES", "प्रॉपर्टी", "Plots • Land • Houses • Apartments", listOf("Residential Plot", "Commercial Plot", "Agricultural Land", "House / Apartment")),
        UnitInfo("DS BUILDING MATERIALS", "बिल्डिंग मटेरियल", "Construction and building materials", listOf("Cement", "TMT / Steel", "Bricks", "Sand & Aggregate")),
        UnitInfo("DS SOLAR TECHNOLOGY", "सोलर टेक्नोलॉजी", "Solar products and installation enquiries", listOf("Solar Panel", "Solar Inverter", "Solar Street Light", "Solar Pump")),
        UnitInfo("DS SMART GADGETS", "स्मार्ट गैजेट्स", "Electronics and smart gadgets", listOf("Smartphone", "Smart Watch", "Bluetooth Earbuds", "Home Electronics")),
        UnitInfo("DS LIFESTYLE BRANDS", "लाइफस्टाइल ब्रांड्स", "Apparels • Footwear • Accessories", listOf("Men's Apparel", "Women's Apparel", "Footwear", "Accessories"))
        // Future re-add options (uncomment when needed):
        // ,UnitInfo("DS POULTRY FARM", "पोल्ट्री फार्म", "Poultry products and supply enquiries", listOf("Poultry Chicks", "Poultry Feed", "Farm Supplies", "Equipment"))
        // ,UnitInfo("DS AQUA", "एक्वा", "Packaged drinking water enquiries", listOf("Packaged Water", "Bulk Water Order", "Bottle Supply", "Event / Function Supply"))
    )
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedUnit by remember { mutableStateOf<UnitInfo?>(null) }
    val cart = remember { mutableStateListOf<CartLine>() }
    var customerName by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var languageHindi by remember { mutableStateOf(false) }

    MaterialTheme(colorScheme = lightColorScheme(
        primary = androidx.compose.ui.graphics.Color(0xFF10284A),
        secondary = androidx.compose.ui.graphics.Color(0xFFC69A43),
        background = androidx.compose.ui.graphics.Color(0xFFF5F6F8)
    )) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("DS KUSHWAHA GROUP", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(if (languageHindi) "आपका भरोसेमंद बिज़नेस ग्रुप" else "Your trusted business group", fontSize = 11.sp)
                        }
                    },
                    actions = {
                        TextButton(onClick = { languageHindi = !languageHindi }) {
                            Text(if (languageHindi) "EN" else "हिंदी", color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(selected = selectedTab == 0, onClick = { selectedTab = 0; selectedUnit = null },
                        icon = { Icon(Icons.Default.Home, null) }, label = { Text(if (languageHindi) "होम" else "Home") })
                    NavigationBarItem(selected = selectedTab == 1, onClick = { selectedTab = 1; selectedUnit = null },
                        icon = { Icon(Icons.Default.Store, null) }, label = { Text(if (languageHindi) "व्यवसाय" else "Businesses") })
                    NavigationBarItem(selected = selectedTab == 2, onClick = { selectedTab = 2; selectedUnit = null },
                        icon = { Icon(Icons.Default.ShoppingCart, null) }, label = { Text("${if (languageHindi) "ऑर्डर" else "Cart"} (${cart.size})") })
                    NavigationBarItem(selected = selectedTab == 3, onClick = { selectedTab = 3; selectedUnit = null },
                        icon = { Icon(Icons.Default.SupportAgent, null) }, label = { Text(if (languageHindi) "संपर्क" else "Contact") })
                }
            }
        ) { padding ->
            when (selectedTab) {
                0 -> HomeScreen(languageHindi, units, onExplore = { selectedTab = 1 }, onSelectUnit = { selectedUnit = it; selectedTab = 1 })
                1 -> if (selectedUnit == null) BusinessScreen(units, languageHindi) { selectedUnit = it }
                     else ProductScreen(selectedUnit!!, languageHindi, onBack = { selectedUnit = null }) { product ->
                         cart.add(CartLine(selectedUnit!!.name, product, 1))
                     }
                2 -> CartScreen(cart, customerName, { customerName = it }, mobile, { mobile = it },
                    address, { address = it }, languageHindi, sendWhatsApp)
                else -> ContactScreen(languageHindi) { sendWhatsApp("Hello DS KUSHWAHA GROUP, I need more information about your businesses.") }
            }
        }
    }
}

@Composable
fun HomeScreen(hindi: Boolean, units: List<UnitInfo>, onExplore: () -> Unit, onSelectUnit: (UnitInfo) -> Unit) {
    var searchText by remember { mutableStateOf("") }
    val visibleUnits = units.filter {
        it.name.contains(searchText, ignoreCase = true) ||
        it.hindi.contains(searchText, ignoreCase = true) ||
        it.detail.contains(searchText, ignoreCase = true)
    }
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(22.dp)) {
                Column(Modifier.padding(22.dp)) {
                    Text("DS KUSHWAHA GROUP", color = androidx.compose.ui.graphics.Color.White, fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(8.dp))
                    Text(if (hindi) "एक समूह • पाँच व्यावसायिक सेवाएँ" else "One Group • Five Business Verticals",
                        color = androidx.compose.ui.graphics.Color.White, fontSize = 15.sp)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = onExplore, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)) {
                        Text(if (hindi) "व्यवसाय देखें" else "Explore Businesses")
                    }
                }
            }
        }
        item {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                placeholder = { Text(if (hindi) "फर्म या सेवा खोजें" else "Search firms and services") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(if (hindi) "DS KUSHWAHA GROUP विशेष" else "DS KUSHWAHA GROUP EXCLUSIVE", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                    Text(if (hindi) "हमारी पाँचों फर्म की सेवाएँ एक ही ऐप में।" else "Explore products and services across all five business units.", fontSize = 14.sp)
                    Text(if (hindi) "ऑफर और कूपन जल्द उपलब्ध होंगे" else "Offers & coupons coming soon", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
        item { Text(if (hindi) "हमारी व्यावसायिक इकाइयाँ" else "Our Business Units", fontSize = 19.sp, fontWeight = FontWeight.Bold) }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(end = 4.dp)) {
                items(visibleUnits) { unit ->
                    Card(
                        onClick = { onSelectUnit(unit) },
                        modifier = Modifier.width(158.dp).height(112.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            Modifier.fillMaxSize().padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(Icons.Default.Business, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(27.dp))
                            Text(unit.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, lineHeight = 15.sp)
                            Text(if (hindi) "देखें  ›" else "Explore  ›", color = MaterialTheme.colorScheme.secondary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
        items(visibleUnits) { unit ->
            Card(onClick = { onSelectUnit(unit) }, shape = RoundedCornerShape(14.dp)) {
                Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Business, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(30.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(unit.name, fontWeight = FontWeight.Bold)
                        Text(unit.detail, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(Icons.Default.ChevronRight, null)
                }
            }
        }
        item { Text(if (hindi) "ऑर्डर और पूछताछ WhatsApp पर भेजें" else "Send orders and enquiries directly on WhatsApp", fontSize = 13.sp) }
    }
}

@Composable
fun BusinessScreen(units: List<UnitInfo>, hindi: Boolean, onSelect: (UnitInfo) -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text(if (hindi) "अपना व्यवसाय चुनें" else "Choose a business", fontSize = 21.sp, fontWeight = FontWeight.Bold) }
        items(units) { unit ->
            Card(onClick = { onSelect(unit) }, shape = RoundedCornerShape(16.dp)) {
                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(unit.name, fontWeight = FontWeight.Bold)
                        Text(unit.hindi, color = MaterialTheme.colorScheme.secondary)
                        Text(unit.detail, fontSize = 12.sp)
                    }
                    Icon(Icons.Default.ChevronRight, null)
                }
            }
        }
    }
}

@Composable
fun ProductScreen(unit: UnitInfo, hindi: Boolean, onBack: () -> Unit, onAdd: (String) -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            TextButton(onClick = onBack) { Text("← ${if (hindi) "वापस" else "Back"}") }
            Text(unit.name, fontSize = 21.sp, fontWeight = FontWeight.Bold)
            Text(if (hindi) "उत्पाद चुनें और पूछताछ सूची में जोड़ें" else "Select an item and add it to your enquiry cart")
            Text(if (hindi) "कीमत और उपलब्धता WhatsApp पर पुष्टि होगी।" else "Price and availability will be confirmed on WhatsApp.",
                fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        items(unit.products) { product ->
            Card(shape = RoundedCornerShape(14.dp)) {
                Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) { Text(product, fontWeight = FontWeight.SemiBold); Text("Price on request", fontSize = 12.sp) }
                    Button(onClick = { onAdd(product) }) { Icon(Icons.Default.Add, null); Text(if (hindi) "जोड़ें" else "Add") }
                }
            }
        }
    }
}

@Composable
fun CartScreen(
    cart: List<CartLine>, name: String, onName: (String) -> Unit, mobile: String, onMobile: (String) -> Unit,
    address: String, onAddress: (String) -> Unit, hindi: Boolean, sendWhatsApp: (String) -> Unit
) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text(if (hindi) "आपकी पूछताछ सूची" else "Your enquiry cart", fontSize = 21.sp, fontWeight = FontWeight.Bold) }
        if (cart.isEmpty()) item { Text(if (hindi) "अभी सूची खाली है। व्यवसाय चुनकर उत्पाद जोड़ें।" else "Your cart is empty. Choose a business and add items.") }
        items(cart) { line ->
            Card {
                Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) { Text(line.product, fontWeight = FontWeight.Bold); Text(line.unit, fontSize = 12.sp) }
                    Text("× ${line.qty}")
                }
            }
        }
        item { Divider(); Text(if (hindi) "ग्राहक की जानकारी" else "Customer details", fontWeight = FontWeight.Bold, fontSize = 18.sp) }
        item { OutlinedTextField(name, onName, label = { Text(if (hindi) "आपका नाम" else "Your name") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(mobile, onMobile, label = { Text(if (hindi) "मोबाइल नंबर" else "Mobile number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(address, onAddress, label = { Text(if (hindi) "डिलीवरी पता / शहर" else "Delivery address / city") },
            modifier = Modifier.fillMaxWidth(), minLines = 2) }
        item {
            Button(
                onClick = {
                    val lines = cart.joinToString("\n") { "• ${it.product} (${it.unit}) x${it.qty}" }
                    val message = "DS KUSHWAHA GROUP - Order / Enquiry\nCustomer: $name\nMobile: $mobile\nAddress: $address\nItems:\n$lines\nPlease confirm price, availability, delivery and payment details."
                    sendWhatsApp(message)
                },
                enabled = cart.isNotEmpty() && name.isNotBlank() && mobile.length >= 10,
                modifier = Modifier.fillMaxWidth()
            ) { Icon(Icons.Default.Send, null); Spacer(Modifier.width(8.dp)); Text(if (hindi) "WhatsApp पर भेजें" else "Send enquiry on WhatsApp") }
        }
        item { Text(if (hindi) "अभी यह ऐप WhatsApp enquiry लेता है; ऑनलाइन भुगतान और लाइव डिलीवरी ट्रैकिंग बाद में merchant/API setup के बाद जोड़ी जाएगी।" else "This version sends enquiries through WhatsApp. Online payment and live delivery tracking require merchant/API setup.", fontSize = 11.sp) }
    }
}

@Composable
fun ContactScreen(hindi: Boolean, onWhatsApp: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(if (hindi) "संपर्क करें" else "Contact us", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("DS KUSHWAHA GROUP", fontWeight = FontWeight.Bold)
        Text("WhatsApp: +91 89339 79022")
        Text("Office: Bank of India ke pichhe, Nadi ke paas, Mauranipur, Dist. Jhansi, U.P. - 284204")
        Button(onClick = onWhatsApp, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Chat, null); Spacer(Modifier.width(8.dp)); Text(if (hindi) "WhatsApp करें" else "Chat on WhatsApp")
        }
    }
}
