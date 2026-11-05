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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.net.URLEncoder

private fun demoPrice(p:String)=mapOf("Residential Plot" to 250000,"Commercial Plot" to 450000,"Agricultural Land" to 180000,"House / Apartment" to 2500000,"Cement" to 390,"TMT / Steel" to 68,"Bricks" to 9,"Sand & Aggregate" to 1800,"Solar Panel" to 12500,"Solar Inverter" to 18500,"Solar Street Light" to 6500,"Solar Pump" to 32000,"Smartphone" to 12999,"Smart Watch" to 1999,"Bluetooth Earbuds" to 1499,"Home Electronics" to 4999,"Men's Apparel" to 799,"Women's Apparel" to 899,"Footwear" to 999,"Accessories" to 399)[p]?:999

private const val WHATSAPP_NUMBER = "918933979022"

data class UnitInfo(val name: String, val hindi: String, val detail: String, val products: List<String>)

data class CartLine(val unit: String, val product: String, val price: Int, val qty: Int)

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
    val context = LocalContext.current
    val preferences = remember { context.getSharedPreferences("dsk_preferences", android.content.Context.MODE_PRIVATE) }
    var languageHindi by remember { mutableStateOf(preferences.getBoolean("language_hindi", false)) }
    fun selectLanguage(hindi: Boolean) {
        languageHindi = hindi
        preferences.edit().putBoolean("language_hindi", hindi).apply()
    }

    MaterialTheme(colorScheme = lightColorScheme(
        primary = androidx.compose.ui.graphics.Color(0xFF10284A),
        secondary = androidx.compose.ui.graphics.Color(0xFFC69A43),
        background = androidx.compose.ui.graphics.Color(0xFFF5F6F8)
    )) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Surface(
                                modifier = Modifier.size(42.dp),
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.primary
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("DS", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                                }
                            }
                            Column {
                                Text("DS KUSHWAHA GROUP", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                                Text(if (languageHindi) "आपका भरोसेमंद बिज़नेस ग्रुप" else "Your trusted business group", fontSize = 10.sp)
                            }
                        }
                    },
                    actions = { }
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
                    NavigationBarItem(selected = selectedTab == 4, onClick = { selectedTab = 4; selectedUnit = null },
                        icon = { Icon(Icons.Default.Person, null) }, label = { Text(if (languageHindi) "प्रोफ़ाइल" else "Profile") })
                }
            }
        ) { padding ->
            when (selectedTab) {
                0 -> HomeScreen(languageHindi, units, onExplore = { selectedTab = 1 }, onSelectUnit = { selectedUnit = it; selectedTab = 1 })
                1 -> if (selectedUnit == null) BusinessScreen(units, languageHindi) { selectedUnit = it }
                     else ProductScreen(selectedUnit!!, languageHindi, onBack = { selectedUnit = null }) { product ->
                         val i=cart.indexOfFirst { it.unit==selectedUnit!!.name && it.product==product }
                         if(i>=0) cart[i]=cart[i].copy(qty=cart[i].qty+1) else cart.add(CartLine(selectedUnit!!.name,product,demoPrice(product),1))
                     }
                2 -> CartScreen(cart, { cart.remove(it) }, { line,delta -> val i=cart.indexOf(line); if(i>=0){val q=line.qty+delta;if(q<=0)cart.removeAt(i) else cart[i]=line.copy(qty=q)} }, customerName, { customerName=it }, mobile, { mobile=it }, address, { address=it }, languageHindi, sendWhatsApp)
                3 -> ContactScreen(languageHindi) { sendWhatsApp("Hello DS KUSHWAHA GROUP, I need more information about your businesses.") }
                else -> ProfileScreen(languageHindi, onLanguageSelected = { selectLanguage(it) })
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
            Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color(0xFF10284A))) {
                Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("DS KUSHWAHA GROUP", color = androidx.compose.ui.graphics.Color(0xFFE2BD70), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("BIG SAVINGS.\nBETTER CHOICES.", color = androidx.compose.ui.graphics.Color.White, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 29.sp)
                    Text("Explore our demo catalogue across five business categories.", color = androidx.compose.ui.graphics.Color.White, fontSize = 13.sp)
                    Button(onClick = onExplore, colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFFC69A43))) { Text("SHOP NOW", color = androidx.compose.ui.graphics.Color(0xFF10284A), fontWeight = FontWeight.Bold) }
                }
            }
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(listOf("✨ Special Deals", "🏷️ Demo Offers", "🚚 Easy Enquiry")) { label ->
                    Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.secondaryContainer) {
                        Text(label, Modifier.padding(horizontal = 14.dp, vertical = 10.dp), fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    }
                }
            }
        }
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
 val prices=mapOf("Residential Plot" to 250000,"Commercial Plot" to 450000,"Agricultural Land" to 180000,"House / Apartment" to 2500000,"Cement" to 390,"TMT / Steel" to 68,"Bricks" to 9,"Sand & Aggregate" to 1800,"Solar Panel" to 12500,"Solar Inverter" to 18500,"Solar Street Light" to 6500,"Solar Pump" to 32000,"Smartphone" to 12999,"Smart Watch" to 1999,"Bluetooth Earbuds" to 1499,"Home Electronics" to 4999,"Men's Apparel" to 799,"Women's Apparel" to 899,"Footwear" to 999,"Accessories" to 399)
 LazyColumn(Modifier.fillMaxSize().padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
 item { TextButton(onClick=onBack){Text("← Back")}; Text(unit.name,fontSize=22.sp,fontWeight=FontWeight.ExtraBold); Text("Popular products • Demo catalogue") }
 items(unit.products){p-> Card(shape=RoundedCornerShape(18.dp),elevation=CardDefaults.cardElevation(3.dp)){Row(Modifier.fillMaxWidth().padding(14.dp),verticalAlignment=Alignment.CenterVertically){
 Surface(Modifier.size(68.dp),shape=RoundedCornerShape(14.dp),color=MaterialTheme.colorScheme.primaryContainer){Box(contentAlignment=Alignment.Center){Icon(Icons.Default.ShoppingBag,null,modifier=Modifier.size(32.dp))}}
 Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)){Text(p,fontWeight=FontWeight.Bold);Text("₹${prices[p]?:999}",fontSize=18.sp,fontWeight=FontWeight.ExtraBold,color=MaterialTheme.colorScheme.primary);Text("Demo price • confirmation required",fontSize=10.sp)}
 FilledTonalButton(onClick={onAdd(p)}){Icon(Icons.Default.Add,null);Text("Add")}
 }}}
 }
}

@Composable
fun CartScreen(cart: List<CartLine>, onRemove:(CartLine)->Unit, onQuantity:(CartLine,Int)->Unit, name:String,onName:(String)->Unit,mobile:String,onMobile:(String)->Unit,address:String,onAddress:(String)->Unit,hindi:Boolean,sendWhatsApp:(String)->Unit){
 val total=cart.sumOf{it.price*it.qty}
 LazyColumn(Modifier.fillMaxSize().padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
 item{Text("My Cart",fontSize=23.sp,fontWeight=FontWeight.ExtraBold)}
 if(cart.isEmpty())item{Text("Your cart is empty. Explore products to get started.")}
 items(cart){line->Card(shape=RoundedCornerShape(18.dp)){Row(Modifier.fillMaxWidth().padding(12.dp),verticalAlignment=Alignment.CenterVertically){
 Column(Modifier.weight(1f)){Text(line.product,fontWeight=FontWeight.Bold);Text(line.unit,fontSize=11.sp);Text("₹${line.price*line.qty}",fontWeight=FontWeight.Bold)}
 IconButton(onClick={onQuantity(line,-1)}){Icon(Icons.Default.RemoveCircleOutline,"Decrease")}
 Text("${line.qty}",fontWeight=FontWeight.Bold)
 IconButton(onClick={onQuantity(line,1)}){Icon(Icons.Default.AddCircle,"Increase")}
 IconButton(onClick={onRemove(line)}){Icon(Icons.Default.DeleteOutline,"Remove")}
 }}}
 if(cart.isNotEmpty())item{Text("Subtotal (demo): ₹$total",fontSize=20.sp,fontWeight=FontWeight.ExtraBold)}
 item{Divider();Text("Customer details",fontWeight=FontWeight.Bold,fontSize=18.sp)}
 item{OutlinedTextField(name,onName,label={Text("Your name")},modifier=Modifier.fillMaxWidth())}
 item{OutlinedTextField(mobile,onMobile,label={Text("Mobile number")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Phone),modifier=Modifier.fillMaxWidth())}
 item{OutlinedTextField(address,onAddress,label={Text("Delivery address / city")},modifier=Modifier.fillMaxWidth(),minLines=2)}
 item{Button(onClick={val lines=cart.joinToString("\n"){"• ${it.product} x${it.qty} = ₹${it.price*it.qty}"};sendWhatsApp("DS KUSHWAHA GROUP Order\nCustomer: $name\nMobile: $mobile\nAddress: $address\n$lines\nDemo subtotal: ₹$total\nPlease confirm final price and availability.")},enabled=cart.isNotEmpty()&&name.isNotBlank()&&mobile.length>=10,modifier=Modifier.fillMaxWidth()){Icon(Icons.Default.Send,null);Text("  Place order / WhatsApp")}}
 item{Text("All displayed prices are demo prices. Final price and availability will be confirmed by seller.",fontSize=11.sp)}
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


@Composable
fun ProfileScreen(hindi: Boolean, onLanguageSelected: (Boolean) -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(if (hindi) "आपकी प्रोफ़ाइल" else "Your Profile", fontSize = 23.sp, fontWeight = FontWeight.Bold)
        Card(shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(34.dp))
                Text("DS KUSHWAHA GROUP", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(if (hindi) "अपनी पसंद की भाषा चुनें" else "Choose your preferred language")
                Text(
                    if (hindi) "ऐप के मेन्यू, प्रोडक्ट और ब्राउज़िंग स्क्रीन आपकी चुनी हुई भाषा में दिखाई देंगी।"
                    else "App menus, product browsing and available content will appear in your selected language.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(if (hindi) "ऐप की भाषा" else "App Language", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Card(
            onClick = { onLanguageSelected(false) },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (!hindi) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
            )
        ) {
            Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("🇬🇧", fontSize = 24.sp)
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text("English", fontWeight = FontWeight.Bold)
                    Text(if (hindi) "Default browsing language" else "Default language", fontSize = 12.sp)
                }
                if (!hindi) Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = MaterialTheme.colorScheme.primary)
            }
        }
        Card(
            onClick = { onLanguageSelected(true) },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (hindi) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
            )
        ) {
            Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("🇮🇳", fontSize = 24.sp)
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text("हिन्दी", fontWeight = FontWeight.Bold)
                    Text("Hindi", fontSize = 12.sp)
                }
                if (hindi) Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = MaterialTheme.colorScheme.primary)
            }
        }
        Text(
            if (hindi) "आपकी भाषा की पसंद इस डिवाइस पर सेव रहेगी।"
            else "Your language preference will be saved on this device and remembered next time you open the app.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
