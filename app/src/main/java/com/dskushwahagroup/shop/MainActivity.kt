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
import coil.compose.AsyncImage

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
        UnitInfo("DS BUILDING MATERIALS", "बिल्डिंग मटेरियल", "Construction and building materials", listOf("Cement 50 kg Bag", "TMT Sariya", "Construction Sand", "Red Bricks", "Stone Aggregate")),
        UnitInfo("DS SOLAR TECHNOLOGY", "सोलर टेक्नोलॉजी", "Solar products and installation enquiries", listOf("Solar Panel 550W", "Solar Inverter", "Tubular Battery", "Solar Water Pump", "Home UPS", "Solar DC Wire", "Mounting Structure")),
        UnitInfo("DS SMART GADGETS", "स्मार्ट गैजेट्स", "Electronics and smart gadgets", listOf("Android Smartphone", "Smart Watch", "Bluetooth Earbuds", "Bluetooth Speaker", "Home Electronics")),
        UnitInfo("DS LIFESTYLE BRANDS", "लाइफस्टाइल ब्रांड्स", "Apparels • Footwear • Accessories", listOf("Premium Formal Shirt", "Premium Polo T-Shirt", "Crew Neck T-Shirt", "Slim Fit Jeans", "Formal Trousers", "Casual Chinos", "Denim Jacket", "Winter Jacket", "Casual Footwear", "Fashion Accessories"))
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
 val prices = mapOf("Cement 50 kg Bag" to 390, "TMT Sariya" to 68, "Construction Sand" to 1800, "Red Bricks" to 9, "Stone Aggregate" to 1600, "Solar Panel 550W" to 12500, "Solar Inverter" to 18500, "Tubular Battery" to 12500, "Solar Water Pump" to 32000, "Home UPS" to 6500, "Solar DC Wire" to 45, "Mounting Structure" to 4500, "Premium Formal Shirt" to 1199, "Premium Polo T-Shirt" to 899, "Crew Neck T-Shirt" to 599, "Slim Fit Jeans" to 1499, "Formal Trousers" to 1299, "Casual Chinos" to 1199, "Denim Jacket" to 1999, "Winter Jacket" to 2499, "Casual Footwear" to 999, "Fashion Accessories" to 399, "Android Smartphone" to 12999, "Smart Watch" to 1999, "Bluetooth Earbuds" to 1499, "Bluetooth Speaker" to 2499, "Home Electronics" to 4999)
 val mrps = mapOf("Premium Formal Shirt" to 1599, "Premium Polo T-Shirt" to 1199, "Crew Neck T-Shirt" to 799, "Slim Fit Jeans" to 1999, "Formal Trousers" to 1699, "Casual Chinos" to 1599, "Denim Jacket" to 2499, "Winter Jacket" to 3199, "Casual Footwear" to 1299, "Fashion Accessories" to 599, "Cement 50 kg Bag" to 430, "TMT Sariya" to 74, "Construction Sand" to 2000, "Red Bricks" to 11, "Solar Panel 550W" to 14500, "Solar Inverter" to 21000, "Tubular Battery" to 14500, "Solar Water Pump" to 36000, "Home UPS" to 7900, "Solar DC Wire" to 55, "Mounting Structure" to 5200)
 fun photoFor(p: String): String {
  val id = when {
   p.contains("Shirt") -> "photo-1596755094514-f87e34085b2c"
   p.contains("T-Shirt") -> "photo-1521572163474-6864f9cf17ab"
   p.contains("Jeans") -> "photo-1542272604-787c3835535d"
   p.contains("Trousers") || p.contains("Chinos") -> "photo-1473966968600-fa801b869a1a"
   p.contains("Jacket") -> "photo-1551028719-00167b16eac5"
   p.contains("Footwear") -> "photo-1542291026-7eec264c27ff"
   p.contains("Solar") || p.contains("Structure") -> "photo-1508514177221-188b1cf16e9d"
   p.contains("Battery") || p.contains("UPS") || p.contains("Inverter") -> "photo-1621905251189-08b45d6a269e"
   p.contains("Pump") -> "photo-1473341304170-971dcad3f4f9"
   p.contains("Smartphone") -> "photo-1511707171634-5f897ff02aa9"
   p.contains("Watch") -> "photo-1523275335684-37898b6baf30"
   p.contains("Earbuds") -> "photo-1606220945770-b5b6c2c55bf1"
   p.contains("Speaker") -> "photo-1608043152269-423dbba4e7e1"
   else -> "photo-1504917595217-d4dc5ebe6122"
  }
  return "https://images.unsplash.com/" + id + "?w=900&auto=format&fit=crop&q=80"
 }
 LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
  item { TextButton(onClick=onBack) { Text("← Back") }; Text(unit.name, fontSize=22.sp, fontWeight=FontWeight.ExtraBold); Text("Product catalogue • sample prices") }
  items(unit.products) { p ->
   val price = prices[p] ?: 999
   val mrp = mrps[p] ?: price
   Card(shape=RoundedCornerShape(18.dp), elevation=CardDefaults.cardElevation(3.dp)) {
    Column {
     AsyncImage(model=photoFor(p), contentDescription=p, modifier=Modifier.fillMaxWidth().height(165.dp), contentScale=androidx.compose.ui.layout.ContentScale.Crop)
     Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement=Arrangement.spacedBy(5.dp)) {
      Text(p, fontWeight=FontWeight.Bold, fontSize=17.sp)
      Text("Product specifications, brand and stock will be confirmed before order.", fontSize=12.sp)
      if (unit.name.contains("LIFESTYLE")) Text(if(p.contains("Jeans") || p.contains("Trousers") || p.contains("Chinos")) "Sizes: 28 • 30 • 32 • 34 • 36 • 38" else "Sizes: S • M • L • XL • XXL", fontSize=11.sp)
      Row(verticalAlignment=Alignment.CenterVertically) {
       Text("₹" + price, fontSize=19.sp, fontWeight=FontWeight.ExtraBold, color=MaterialTheme.colorScheme.primary)
       Spacer(Modifier.width(8.dp))
       if (mrp > price) Text("₹" + mrp, fontSize=12.sp, color=androidx.compose.ui.graphics.Color.Gray)
       Spacer(Modifier.weight(1f))
       Text("sample rate", fontSize=10.sp)
      }
      if (mrp > price) Text(((mrp-price)*100/mrp).toString() + "% sample discount", fontSize=11.sp, color=androidx.compose.ui.graphics.Color(0xFF17834B))
      Text("Illustrative photo and sample price only. Confirm taxes, transport and final quote.", fontSize=10.sp)
      FilledTonalButton(onClick={onAdd(p)}, modifier=Modifier.fillMaxWidth()) { Icon(Icons.Default.AddShoppingCart,null); Text("  Add to cart") }
     }
    }
   }
  }
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
    var customerName by remember { mutableStateOf("") }
    var favourite by remember { mutableStateOf("DS LIFESTYLE BRANDS") }
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(13.dp)) {
        Text(if (hindi) "आपकी प्रोफ़ाइल" else "Your Profile", fontSize = 23.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(customerName, { customerName = it }, modifier = Modifier.fillMaxWidth(), label = { Text(if (hindi) "आपका नाम" else "Your name") }, singleLine = true)
        Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
            Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("DS REWARDS WALLET", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                Text("0 Coins", color = androidx.compose.ui.graphics.Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                Text("Coins will be credited against verified orders after the rewards programme is activated.", color = androidx.compose.ui.graphics.Color.White, fontSize = 11.sp)
            }
        }
        Text(if (hindi) "आपके लिए ऑफर" else "OFFERS FOR YOU", fontWeight = FontWeight.Bold, fontSize = 17.sp)
        Text("Choose your favourite business to preview relevant deals.", fontSize = 12.sp)
        listOf("DS PROPERTIES", "DS BUILDING MATERIALS", "DS SOLAR TECHNOLOGY", "DS SMART GADGETS", "DS LIFESTYLE BRANDS").forEach { firm ->
            FilterChip(selected = favourite == firm, onClick = { favourite = firm }, label = { Text(firm) })
        }
        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
            Column(Modifier.padding(15.dp)) {
                Text(if (customerName.isBlank()) "Welcome offer preview" else "Hello, $customerName!", fontWeight = FontWeight.Bold)
                Text("Your ${favourite.removePrefix("DS ")} deals will appear here. Coupon redemption and live personalised offers require the connected admin store.", fontSize = 12.sp)
            }
        }
        Divider()
        Text(if (hindi) "ऐप की भाषा" else "App Language", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            FilterChip(selected = !hindi, onClick = { onLanguageSelected(false) }, label = { Text("English") })
            FilterChip(selected = hindi, onClick = { onLanguageSelected(true) }, label = { Text("हिन्दी") })
        }
        Text("We do not show invented customer counts, fake reviews or unverified years in business. Coins and offers are preview-only until live orders and admin systems are connected.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
