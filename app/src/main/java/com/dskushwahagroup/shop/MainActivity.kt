package com.dskushwahagroup.shop

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import kotlinx.coroutines.delay
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

private fun demoPrice(p:String)=mapOf(
 "Residential Plot" to 250000, "Commercial Plot" to 450000, "Agricultural Land" to 180000, "House / Apartment" to 2500000,
 "Cement 50 kg Bag" to 390, "TMT Sariya" to 68, "Construction Sand" to 1800, "Red Bricks" to 9, "Stone Aggregate" to 1600,
 "Solar Panel 550W" to 12500, "Solar Inverter" to 18500, "Tubular Battery" to 12500, "Solar Water Pump" to 32000, "Home UPS" to 6500, "Solar DC Wire" to 45, "Mounting Structure" to 4500,
 "Android Smartphone" to 12999, "Smart Watch" to 1999, "Bluetooth Earbuds" to 1499, "Bluetooth Speaker" to 2499, "Home Electronics" to 4999,
 "Premium Formal Shirt" to 1199, "Premium Polo T-Shirt" to 899, "Crew Neck T-Shirt" to 599, "Slim Fit Jeans" to 1499, "Formal Trousers" to 1299, "Casual Chinos" to 1199, "Denim Jacket" to 1999, "Winter Jacket" to 2499, "Casual Footwear" to 999, "Fashion Accessories" to 399
)[p]?:999

private fun productMrp(p:String):Int = mapOf(
 "Premium Formal Shirt" to 1599, "Premium Polo T-Shirt" to 1199, "Crew Neck T-Shirt" to 799, "Slim Fit Jeans" to 1999, "Formal Trousers" to 1699, "Casual Chinos" to 1599, "Denim Jacket" to 2499, "Winter Jacket" to 3199, "Casual Footwear" to 1299, "Fashion Accessories" to 599,
 "Cement 50 kg Bag" to 430, "TMT Sariya" to 74, "Construction Sand" to 2000, "Red Bricks" to 11, "Solar Panel 550W" to 14500, "Solar Inverter" to 21000, "Tubular Battery" to 14500, "Solar Water Pump" to 36000, "Home UPS" to 7900, "Solar DC Wire" to 55, "Mounting Structure" to 5200
)[p] ?: demoPrice(p)

private fun productPhoto(p:String):String {
 val id=when {
  p.contains("Residential Plot") || p.contains("Agricultural Land") -> "photo-1500382017468-9049fed747ef"
  p.contains("Commercial Plot") -> "photo-1486406146926-c627a92ad1ab"
  p.contains("House") || p.contains("Apartment") -> "photo-1600585154340-be6161a56a0c"
  p.contains("Cement") -> "photo-1518709268805-4e9042af9f23"
  p.contains("TMT") || p.contains("Sariya") -> "photo-1504917595217-d4dc5ebe6122"
  p.contains("Sand") -> "photo-1504307651254-35680f356dfd"
  p.contains("Bricks") -> "photo-1590725140246-20acb1fbe6e2"
  p.contains("Aggregate") || p.contains("Stone") -> "photo-1518709268805-4e9042af9f23"
  p.contains("Solar Panel") -> "photo-1508514177221-188b1cf16e9d"
  p.contains("Inverter") -> "photo-1621905251189-08b45d6a269e"
  p.contains("Battery") -> "photo-1609091839311-d5365f9ff1c5"
  p.contains("Pump") -> "photo-1572120360610-d971b9d7767c"
  p.contains("Home UPS") -> "photo-1558008258-3256797b43f3"
  p.contains("Solar DC Wire") -> "photo-1558618666-fcd25c85cd64"
  p.contains("Mounting Structure") -> "photo-1497440001374-f26997328c1b"
  p.contains("Formal Shirt") -> "photo-1596755094514-f87e34085b2c"
  p.contains("Polo T-Shirt") -> "photo-1581655353564-df123a1eb820"
  p.contains("Crew Neck T-Shirt") -> "photo-1521572163474-6864f9cf17ab"
  p.contains("Jeans") -> "photo-1542272604-787c3835535d"
  p.contains("Trousers") -> "photo-1473966968600-fa801b869a1a"
  p.contains("Chinos") -> "photo-1515886657613-9f3515b0c78f"
  p.contains("Denim Jacket") -> "photo-1551028719-00167b16eac5"
  p.contains("Winter Jacket") -> "photo-1544923246-77307dd654cb"
  p.contains("Footwear") -> "photo-1542291026-7eec264c27ff"
  p.contains("Accessories") -> "photo-1523170335258-f5ed11844a49"
  p.contains("Smartphone") -> "photo-1511707171634-5f897ff02aa9"
  p.contains("Watch") -> "photo-1523275335684-37898b6baf30"
  p.contains("Earbuds") -> "photo-1606220945770-b5b6c2c55bf1"
  p.contains("Speaker") -> "photo-1608043152269-423dbba4e7e1"
  else -> "photo-1498049794561-7780e7231661"
 }
 return "https://images.unsplash.com/$id?w=1000&auto=format&fit=crop&q=88"
}

private fun productDescription(p:String):String = when {
 p.contains("Solar Panel") -> "550W high-efficiency solar module • system sizing and installation quoted separately."
 p.contains("Inverter") -> "Solar inverter options • capacity, phase and compatibility confirmed before order."
 p.contains("Battery") -> "Tubular battery • backup capacity and warranty depend on selected model."
 p.contains("Shirt") -> "Smart everyday style • fabric, colour and available sizes confirmed before dispatch."
 p.contains("T-Shirt") -> "Comfort-fit casual essential • multiple colours and sizes subject to stock."
 p.contains("Jeans") || p.contains("Trousers") || p.contains("Chinos") -> "Everyday fit and finish • check waist size and colour availability."
 p.contains("Smartphone") -> "Model, RAM, storage, warranty and final price confirmed before purchase."
 p.contains("Watch") || p.contains("Earbuds") || p.contains("Speaker") -> "Gadget specifications and warranty depend on the selected model."
 p.contains("Cement") -> "50 kg bag • brand, grade, quantity and delivery charges confirmed on enquiry."
 p.contains("TMT") -> "Steel rate varies by grade, diameter, quantity and market price."
 p.contains("Sand") || p.contains("Aggregate") || p.contains("Bricks") -> "Bulk material • quantity, quality and transport quoted by location."
 p.contains("Plot") || p.contains("Land") || p.contains("House") -> "Property details, title, location and site visit subject to verification."
 else -> "Product details, brand, stock and final quote will be confirmed before order."
}

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
    var showCheckout by remember { mutableStateOf(false) }
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
        background = androidx.compose.ui.graphics.Color(0xFFF5F6F8),
        surface = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
        surfaceVariant = androidx.compose.ui.graphics.Color(0xFFF0F2F6),
        primaryContainer = androidx.compose.ui.graphics.Color(0xFFE7EDF5),
        secondaryContainer = androidx.compose.ui.graphics.Color(0xFFFFF3D9),
        onSurface = androidx.compose.ui.graphics.Color(0xFF182334)
    )) {
        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color(0xFFF5F6F8),
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = androidx.compose.ui.graphics.Color.White),
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
                    NavigationBarItem(selected = selectedTab == 2, onClick = { selectedTab = 2; selectedUnit = null; showCheckout = false },
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
                2 -> if (showCheckout) CheckoutScreen(cart, customerName, { customerName=it }, mobile, { mobile=it }, address, { address=it }, languageHindi, onBack={ showCheckout=false }, sendWhatsApp=sendWhatsApp) else CartScreen(cart, { cart.remove(it) }, { line,delta -> val i=cart.indexOf(line); if(i>=0){val q=line.qty+delta;if(q<=0)cart.removeAt(i) else cart[i]=line.copy(qty=q)} }, languageHindi, onCheckout={ showCheckout=true })
                3 -> ContactScreen(languageHindi) { sendWhatsApp("Hello DS KUSHWAHA GROUP, I need more information about your businesses.") }
                else -> ProfileScreen(languageHindi, onLanguageSelected = { selectLanguage(it) })
            }
        }
    }
}

@Composable
fun HomeScreen(hindi: Boolean, units: List<UnitInfo>, onExplore: () -> Unit, onSelectUnit: (UnitInfo) -> Unit) {
 var searchText by remember { mutableStateOf("") }
 val visibleUnits=units.filter{it.name.contains(searchText,true)||it.hindi.contains(searchText,true)||it.detail.contains(searchText,true)}
 val bannerState= rememberLazyListState()
 val titles=listOf("BIG SAVINGS. BETTER CHOICES.","POWER YOUR HOME WITH SOLAR.","STYLE THAT SPEAKS FOR YOU.","BUILD BETTER. LIVE BETTER.")
 val subtitles=listOf("Explore five business verticals in one place.","Panels, inverters and batteries for your energy needs.","Discover apparel, footwear and everyday essentials.","Property and building materials for your next project.")
 val images=listOf("photo-1497366216548-37526070297c","photo-1508514177221-188b1cf16e9d","photo-1529139574466-a303027c1d8b","photo-1600585154340-be6161a56a0c")
 LaunchedEffect(Unit){while(true){delay(3800);bannerState.animateScrollToItem((bannerState.firstVisibleItemIndex+1)%titles.size)}}
 LazyColumn(Modifier.fillMaxSize().padding(horizontal=16.dp),verticalArrangement=Arrangement.spacedBy(16.dp)){
  item{
   Row(Modifier.fillMaxWidth().padding(top=8.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.SpaceBetween){
    Column(verticalArrangement=Arrangement.spacedBy(2.dp)){Text(if(hindi) "नमस्ते 👋" else "WELCOME TO",fontSize=10.sp,fontWeight=FontWeight.Bold,color=MaterialTheme.colorScheme.secondary);Text("DS KUSHWAHA GROUP",fontSize=19.sp,fontWeight=FontWeight.ExtraBold,color=MaterialTheme.colorScheme.primary);Text(if(hindi) "एक समूह • अनेक समाधान" else "One group. Multiple solutions.",fontSize=11.sp,color=MaterialTheme.colorScheme.onSurfaceVariant)}
    Surface(shape=RoundedCornerShape(15.dp),color=MaterialTheme.colorScheme.primary){Text("DSKG",Modifier.padding(horizontal=13.dp,vertical=12.dp),fontWeight=FontWeight.ExtraBold,color=androidx.compose.ui.graphics.Color(0xFFFFD98A))}
   }
  }
  item{
   LazyRow(state=bannerState,horizontalArrangement=Arrangement.spacedBy(12.dp),contentPadding=PaddingValues(end=5.dp)){
    items(titles.size){i->
     Card(shape=RoundedCornerShape(24.dp),modifier=Modifier.width(315.dp).height(220.dp)){
      Box(Modifier.fillMaxSize()){
       AsyncImage(model="https://images.unsplash.com/${images[i]}?w=1000&auto=format&fit=crop&q=88",contentDescription=null,modifier=Modifier.fillMaxSize(),contentScale=androidx.compose.ui.layout.ContentScale.Crop)
       Box(Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Color(0xC910284A)))
       Column(Modifier.fillMaxSize().padding(20.dp),verticalArrangement=Arrangement.SpaceBetween){
        Text("DS KUSHWAHA GROUP  •  EXCLUSIVE",color=androidx.compose.ui.graphics.Color(0xFFFFD98A),fontSize=10.sp,fontWeight=FontWeight.Bold)
        Column(verticalArrangement=Arrangement.spacedBy(7.dp)){Text(titles[i],color=androidx.compose.ui.graphics.Color.White,fontSize=24.sp,fontWeight=FontWeight.ExtraBold,lineHeight=28.sp);Text(subtitles[i],color=androidx.compose.ui.graphics.Color.White,fontSize=12.sp)}
        Button(onClick=onExplore,colors=ButtonDefaults.buttonColors(containerColor=androidx.compose.ui.graphics.Color(0xFFE0B65C)),shape=RoundedCornerShape(13.dp)){Text(if(hindi) "अभी देखें" else "Explore now",color=androidx.compose.ui.graphics.Color(0xFF10284A),fontWeight=FontWeight.Bold);Icon(Icons.Default.ArrowForward,null,tint=androidx.compose.ui.graphics.Color(0xFF10284A))}
       }
      }
     }
    }
   }
   Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.Center){repeat(titles.size){i->Surface(Modifier.padding(horizontal=3.dp).size(if(bannerState.firstVisibleItemIndex==i)18.dp else 6.dp,6.dp),shape=RoundedCornerShape(50),color=if(bannerState.firstVisibleItemIndex==i)MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant){} }}
  }
  item{
   OutlinedTextField(searchText,{searchText=it},modifier=Modifier.fillMaxWidth(),singleLine=true,shape=RoundedCornerShape(16.dp),placeholder={Text(if(hindi) "व्यवसाय या सेवा खोजें" else "Search businesses or services")},leadingIcon={Icon(Icons.Default.Search,null)})
  }
  item{
   Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){
    listOf("Easy enquiries","Customer support","Simple ordering").forEach{label->Surface(Modifier.weight(1f),shape=RoundedCornerShape(13.dp),color=MaterialTheme.colorScheme.surfaceVariant){Column(Modifier.padding(vertical=10.dp,horizontal=4.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(4.dp)){Icon(Icons.Default.CheckCircle,null,tint=MaterialTheme.colorScheme.secondary,modifier=Modifier.size(18.dp));Text(label,fontSize=9.sp,fontWeight=FontWeight.SemiBold)}}}
   }
  }
  item{Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){Text(if(hindi) "हमारे व्यवसाय" else "Shop by business",fontSize=20.sp,fontWeight=FontWeight.ExtraBold);TextButton(onClick=onExplore){Text(if(hindi) "सभी देखें" else "View all")}}}
  items(visibleUnits){unit->
   Card(onClick={onSelectUnit(unit)},shape=RoundedCornerShape(20.dp),elevation=CardDefaults.cardElevation(2.dp)){
    Row(Modifier.fillMaxWidth().padding(15.dp),verticalAlignment=Alignment.CenterVertically){
     Surface(shape=RoundedCornerShape(15.dp),color=MaterialTheme.colorScheme.primaryContainer,modifier=Modifier.size(54.dp)){Box(contentAlignment=Alignment.Center){Icon(Icons.Default.Store,null,tint=MaterialTheme.colorScheme.primary,modifier=Modifier.size(27.dp))}}
     Spacer(Modifier.width(13.dp))
     Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(3.dp)){Text(unit.name,fontWeight=FontWeight.ExtraBold,fontSize=13.sp);Text(unit.detail,fontSize=11.sp,color=MaterialTheme.colorScheme.onSurfaceVariant);Text("${unit.products.size} catalogue items",fontSize=10.sp,color=MaterialTheme.colorScheme.secondary)}
     Icon(Icons.Default.ChevronRight,null,tint=MaterialTheme.colorScheme.secondary)
    }
   }
  }
  if(visibleUnits.isEmpty())item{Text(if(hindi) "कोई व्यवसाय नहीं मिला।" else "No matching business found.")}
  item{
   Card(shape=RoundedCornerShape(20.dp),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primary)){
    Row(Modifier.fillMaxWidth().padding(18.dp),verticalAlignment=Alignment.CenterVertically){
     Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(5.dp)){Text(if(hindi) "मदद चाहिए?" else "Need assistance?",color=androidx.compose.ui.graphics.Color(0xFFFFD98A),fontWeight=FontWeight.Bold);Text(if(hindi) "उत्पाद, उपलब्धता या कीमत के लिए हमारी टीम से संपर्क करें।" else "Talk to our team about products, availability or a custom quote.",color=androidx.compose.ui.graphics.Color.White,fontSize=12.sp)}
     Icon(Icons.Default.SupportAgent,null,tint=androidx.compose.ui.graphics.Color.White,modifier=Modifier.size(34.dp))
    }
   }
  }
  item{Text("Sample catalogue • Product availability, rates and delivery are confirmed by the DS team.",fontSize=10.sp,color=MaterialTheme.colorScheme.onSurfaceVariant,modifier=Modifier.padding(bottom=12.dp))}
 }
}

@Composable
fun BusinessScreen(units: List<UnitInfo>, hindi: Boolean, onSelect: (UnitInfo) -> Unit) {
 LazyColumn(Modifier.fillMaxSize().padding(16.dp),verticalArrangement=Arrangement.spacedBy(13.dp)){
  item{Text(if(hindi) "हमारे व्यवसाय" else "Explore our businesses",fontSize=24.sp,fontWeight=FontWeight.ExtraBold,color=MaterialTheme.colorScheme.primary);Text(if(hindi) "अपनी जरूरत के अनुसार एक श्रेणी चुनें।" else "Choose a category to browse its products and services.",fontSize=12.sp,color=MaterialTheme.colorScheme.onSurfaceVariant)}
  items(units){unit->
   Card(onClick={onSelect(unit)},shape=RoundedCornerShape(22.dp),elevation=CardDefaults.cardElevation(2.dp)){
    Column(Modifier.fillMaxWidth().padding(17.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
     Row(verticalAlignment=Alignment.CenterVertically){
      Surface(shape=RoundedCornerShape(16.dp),color=MaterialTheme.colorScheme.primary,modifier=Modifier.size(52.dp)){Box(contentAlignment=Alignment.Center){Icon(Icons.Default.Store,null,tint=androidx.compose.ui.graphics.Color(0xFFFFD98A),modifier=Modifier.size(27.dp))}}
      Spacer(Modifier.width(12.dp))
      Column(Modifier.weight(1f)){Text(unit.name,fontWeight=FontWeight.ExtraBold,fontSize=14.sp,color=MaterialTheme.colorScheme.primary);Text(unit.hindi,fontSize=11.sp,color=MaterialTheme.colorScheme.secondary)}
      Icon(Icons.Default.ChevronRight,null,tint=MaterialTheme.colorScheme.secondary)
     }
     Text(unit.detail,fontSize=12.sp,color=MaterialTheme.colorScheme.onSurfaceVariant)
     Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){
      Text("${unit.products.size} catalogue items",fontSize=10.sp,fontWeight=FontWeight.SemiBold,color=MaterialTheme.colorScheme.secondary)
      Text(if(hindi) "कैटलॉग खोलें  →" else "Open catalogue  →",fontSize=11.sp,fontWeight=FontWeight.Bold,color=MaterialTheme.colorScheme.primary)
     }
    }
   }
  }
 }
}
@Composable
fun ProductScreen(unit: UnitInfo, hindi: Boolean, onBack: () -> Unit, onAdd: (String) -> Unit) {
 var search by remember { mutableStateOf("") }
 val products=unit.products.filter{it.contains(search,ignoreCase=true)}
 LazyColumn(Modifier.fillMaxSize().padding(horizontal=16.dp),verticalArrangement=Arrangement.spacedBy(13.dp)){
  item{
   TextButton(onClick=onBack){Icon(Icons.Default.ArrowBack,null);Text(if(hindi) "वापस" else "Back")}
   Text(unit.name,fontSize=23.sp,fontWeight=FontWeight.ExtraBold,color=MaterialTheme.colorScheme.primary)
   Text(unit.detail,fontSize=13.sp,color=MaterialTheme.colorScheme.onSurfaceVariant)
   Spacer(Modifier.height(8.dp))
   OutlinedTextField(search,{search=it},modifier=Modifier.fillMaxWidth(),singleLine=true,shape=RoundedCornerShape(16.dp),placeholder={Text(if(hindi) "उत्पाद खोजें" else "Search products")},leadingIcon={Icon(Icons.Default.Search,null)})
   Text("${products.size} products • Illustrative catalogue",fontSize=11.sp,color=MaterialTheme.colorScheme.onSurfaceVariant)
  }
  items(products){p->
   val price=demoPrice(p);val mrp=productMrp(p)
   Card(shape=RoundedCornerShape(22.dp),elevation=CardDefaults.cardElevation(3.dp)){
    Column {
     AsyncImage(model=productPhoto(p),contentDescription=p,modifier=Modifier.fillMaxWidth().height(190.dp),contentScale=androidx.compose.ui.layout.ContentScale.Crop)
     Column(Modifier.fillMaxWidth().padding(15.dp),verticalArrangement=Arrangement.spacedBy(7.dp)){
      Text(p,fontSize=17.sp,fontWeight=FontWeight.Bold)
      Text(productDescription(p),fontSize=12.sp,color=MaterialTheme.colorScheme.onSurfaceVariant)
      if(unit.name.contains("LIFESTYLE"))Text(if(p.contains("Jeans")||p.contains("Trousers")||p.contains("Chinos")) "Sizes: 28 • 30 • 32 • 34 • 36 • 38" else "Sizes: S • M • L • XL • XXL",fontSize=11.sp)
      Row(verticalAlignment=Alignment.CenterVertically){
       Text("₹$price",fontSize=21.sp,fontWeight=FontWeight.ExtraBold,color=MaterialTheme.colorScheme.primary)
       Spacer(Modifier.width(8.dp))
       if(mrp>price)Text("₹$mrp",fontSize=12.sp,color=androidx.compose.ui.graphics.Color.Gray)
       Spacer(Modifier.weight(1f))
       if(mrp>price)Text("${(mrp-price)*100/mrp}% OFF",fontSize=11.sp,fontWeight=FontWeight.Bold,color=androidx.compose.ui.graphics.Color(0xFF16834A))
      }
      Text("Sample price • taxes, delivery and availability to be confirmed.",fontSize=10.sp,color=MaterialTheme.colorScheme.onSurfaceVariant)
      Button(onClick={onAdd(p)},modifier=Modifier.fillMaxWidth().height(48.dp),shape=RoundedCornerShape(14.dp)){Icon(Icons.Default.AddShoppingCart,null);Text(if(hindi) "कार्ट में जोड़ें" else "Add to cart",fontWeight=FontWeight.Bold)}
     }
    }
   }
  }
  if(products.isEmpty())item{Text(if(hindi) "कोई उत्पाद नहीं मिला।" else "No matching products found.")}
 }
}
@Composable
fun CartScreen(cart: List<CartLine>, onRemove:(CartLine)->Unit, onQuantity:(CartLine,Int)->Unit, hindi:Boolean, onCheckout:()->Unit){
 val total=cart.sumOf{it.price*it.qty}
 LazyColumn(Modifier.fillMaxSize().padding(16.dp),verticalArrangement=Arrangement.spacedBy(13.dp)){
  item{Text(if(hindi) "आपका कार्ट" else "My Cart",fontSize=26.sp,fontWeight=FontWeight.ExtraBold,color=MaterialTheme.colorScheme.primary);Text(if(hindi) "आपके चुने हुए उत्पाद" else "${cart.sumOf{it.qty}} items selected",fontSize=12.sp,color=MaterialTheme.colorScheme.onSurfaceVariant)}
  if(cart.isEmpty())item{Card(shape=RoundedCornerShape(24.dp)){Column(Modifier.fillMaxWidth().padding(30.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(8.dp)){Icon(Icons.Default.ShoppingCart,null,modifier=Modifier.size(52.dp),tint=MaterialTheme.colorScheme.secondary);Text(if(hindi) "आपका कार्ट खाली है" else "Your cart is empty",fontSize=19.sp,fontWeight=FontWeight.Bold);Text(if(hindi) "खरीदारी से उत्पाद जोड़ें।" else "Add products from a business catalogue to see them here.",fontSize=12.sp)}}}
  items(cart){line->Card(shape=RoundedCornerShape(20.dp),elevation=CardDefaults.cardElevation(2.dp)){Row(Modifier.fillMaxWidth().padding(11.dp),verticalAlignment=Alignment.CenterVertically){
   AsyncImage(model=productPhoto(line.product),contentDescription=line.product,modifier=Modifier.size(88.dp),contentScale=androidx.compose.ui.layout.ContentScale.Crop)
   Spacer(Modifier.width(12.dp))
   Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(4.dp)){
    Text(line.product,fontWeight=FontWeight.Bold,fontSize=14.sp)
    Text(line.unit,fontSize=10.sp,color=MaterialTheme.colorScheme.onSurfaceVariant)
    Text("₹${line.price*line.qty}",fontWeight=FontWeight.ExtraBold,color=MaterialTheme.colorScheme.primary,fontSize=17.sp)
    Row(verticalAlignment=Alignment.CenterVertically){
     IconButton(onClick={onQuantity(line,-1)},modifier=Modifier.size(30.dp)){Icon(Icons.Default.RemoveCircleOutline,"Decrease")}
     Text("${line.qty}",fontWeight=FontWeight.Bold)
     IconButton(onClick={onQuantity(line,1)},modifier=Modifier.size(30.dp)){Icon(Icons.Default.AddCircle,"Increase")}
     Spacer(Modifier.width(5.dp))
     TextButton(onClick={onRemove(line)}){Text(if(hindi) "हटाएँ" else "Remove",fontSize=11.sp)}
    }
   }
  }}}
  if(cart.isNotEmpty()){
   item{Card(shape=RoundedCornerShape(22.dp),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)){Column(Modifier.fillMaxWidth().padding(18.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){
    Text(if(hindi) "ऑर्डर सारांश" else "Order summary",fontWeight=FontWeight.Bold,fontSize=17.sp)
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text(if(hindi) "कुल आइटम" else "Items");Text("${cart.sumOf{it.qty}}")}
    Divider()
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text(if(hindi) "सबटोटल (सैंपल)" else "Subtotal (sample)",fontWeight=FontWeight.Bold);Text("₹$total",fontWeight=FontWeight.ExtraBold,fontSize=21.sp)}
    Text(if(hindi) "डिलीवरी और अंतिम कीमत की पुष्टि चेकआउट पर होगी।" else "Delivery charges and final quote are confirmed at checkout.",fontSize=11.sp)
   }}}
   item{Button(onClick=onCheckout,modifier=Modifier.fillMaxWidth().height(55.dp),shape=RoundedCornerShape(16.dp)){Text(if(hindi) "चेकआउट के लिए आगे बढ़ें" else "Proceed to Checkout",fontWeight=FontWeight.Bold,fontSize=16.sp);Spacer(Modifier.width(8.dp));Icon(Icons.Default.ArrowForward,null)}}
  }
 }
}
@Composable
fun CheckoutScreen(cart:List<CartLine>,name:String,onName:(String)->Unit,mobile:String,onMobile:(String)->Unit,address:String,onAddress:(String)->Unit,hindi:Boolean,onBack:()->Unit,sendWhatsApp:(String)->Unit){
 val total=cart.sumOf{it.price*it.qty}
 LazyColumn(Modifier.fillMaxSize().padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
  item{TextButton(onClick=onBack){Icon(Icons.Default.ArrowBack,null);Text(if(hindi) "कार्ट पर वापस" else "Back to cart")};Text(if(hindi) "चेकआउट" else "Checkout",fontSize=25.sp,fontWeight=FontWeight.ExtraBold)}
  item{Card(shape=RoundedCornerShape(18.dp)){Column(Modifier.fillMaxWidth().padding(15.dp)){Text(if(hindi) "आपके चुने हुए उत्पाद" else "Your selected products",fontWeight=FontWeight.Bold);cart.forEach{Text("${it.product} × ${it.qty}  —  ₹${it.price*it.qty}",fontSize=13.sp)};Divider();Text("Total: ₹$total",fontWeight=FontWeight.ExtraBold,fontSize=20.sp)}}}
  item{Text(if(hindi) "डिलीवरी विवरण" else "Delivery details",fontWeight=FontWeight.Bold,fontSize=18.sp)}
  item{OutlinedTextField(name,onName,label={Text(if(hindi) "पूरा नाम" else "Full name")},modifier=Modifier.fillMaxWidth(),singleLine=true)}
  item{OutlinedTextField(mobile,onMobile,label={Text(if(hindi) "मोबाइल नंबर" else "Mobile number")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Phone),modifier=Modifier.fillMaxWidth(),singleLine=true)}
  item{OutlinedTextField(address,onAddress,label={Text(if(hindi) "पूरा पता / शहर / पिनकोड" else "Full address / city / PIN code")},modifier=Modifier.fillMaxWidth(),minLines=3)}
  item{Button(onClick={val lines=cart.joinToString("\n"){"• ${it.product} x${it.qty} = ₹${it.price*it.qty}"};sendWhatsApp("DS KUSHWAHA GROUP Order\nCustomer: $name\nMobile: $mobile\nAddress: $address\n$lines\nDemo subtotal: ₹$total\nPlease confirm final price and availability.")},enabled=cart.isNotEmpty()&&name.isNotBlank()&&mobile.filter{it.isDigit()}.length>=10&&address.isNotBlank(),modifier=Modifier.fillMaxWidth().height(54.dp),shape=RoundedCornerShape(16.dp)){Icon(Icons.Default.Send,null);Text(if(hindi) "WhatsApp पर ऑर्डर भेजें" else "Place Order via WhatsApp",fontWeight=FontWeight.Bold)}}
  item{Text(if(hindi) "अंतिम कीमत और उपलब्धता विक्रेता द्वारा पुष्टि की जाएगी।" else "Final price and availability will be confirmed by the seller.",fontSize=11.sp)}
 }
}

@Composable
fun ContactScreen(hindi: Boolean, onWhatsApp: () -> Unit) {
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(14.dp)){
  item{Text(if(hindi) "हमसे संपर्क करें" else "We're here to help",fontSize=25.sp,fontWeight=FontWeight.ExtraBold,color=MaterialTheme.colorScheme.primary);Text(if(hindi) "उत्पाद, उपलब्धता, कीमत और बिज़नेस पूछताछ के लिए संपर्क करें।" else "Get in touch for product details, availability, pricing and business enquiries.",fontSize=12.sp,color=MaterialTheme.colorScheme.onSurfaceVariant)}
  item{Card(shape=RoundedCornerShape(22.dp),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primary)){
   Column(Modifier.fillMaxWidth().padding(20.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
    Text("DS KUSHWAHA GROUP",fontSize=19.sp,fontWeight=FontWeight.ExtraBold,color=androidx.compose.ui.graphics.Color.White)
    Text(if(hindi) "आपकी पूछताछ हमारी टीम तक सीधे पहुँचेगी।" else "Send your enquiry directly to our team.",fontSize=12.sp,color=androidx.compose.ui.graphics.Color.White)
    Button(onClick=onWhatsApp,modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(14.dp),colors=ButtonDefaults.buttonColors(containerColor=androidx.compose.ui.graphics.Color(0xFFE0B65C))){Icon(Icons.Default.Chat,null,tint=androidx.compose.ui.graphics.Color(0xFF10284A));Spacer(Modifier.width(8.dp));Text(if(hindi) "WhatsApp पर बात करें" else "Chat on WhatsApp",color=androidx.compose.ui.graphics.Color(0xFF10284A),fontWeight=FontWeight.Bold)}
   }
  }}
  item{Card(shape=RoundedCornerShape(20.dp)){Column(Modifier.fillMaxWidth().padding(17.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
   Row(verticalAlignment=Alignment.CenterVertically){Icon(Icons.Default.Phone,null,tint=MaterialTheme.colorScheme.secondary);Spacer(Modifier.width(10.dp));Column{Text("Customer enquiries",fontWeight=FontWeight.Bold);Text("+91 89339 79022",fontSize=13.sp)}}
   Divider()
   Row(verticalAlignment=Alignment.CenterVertically){Icon(Icons.Default.Phone,null,tint=MaterialTheme.colorScheme.secondary);Spacer(Modifier.width(10.dp));Column{Text("Additional contact",fontWeight=FontWeight.Bold);Text("+91 96215 81509",fontSize=13.sp)}}
   Divider()
   Row(verticalAlignment=Alignment.Top){Icon(Icons.Default.LocationOn,null,tint=MaterialTheme.colorScheme.secondary);Spacer(Modifier.width(10.dp));Column{Text(if(hindi) "कार्यालय का पता" else "Office address",fontWeight=FontWeight.Bold);Text("Bank of India ke bagal mein, Nadi Pull ke paas, Mauranipur, Uttar Pradesh - 284204",fontSize=12.sp)}}
  }}}
  item{Text("Business hours and service availability are confirmed by the team when you contact us.",fontSize=10.sp,color=MaterialTheme.colorScheme.onSurfaceVariant)}
 }
}
undefined@Composable
fun ProfileScreen(hindi: Boolean, onLanguageSelected: (Boolean) -> Unit) {
 val context=LocalContext.current
 val prefs=remember{context.getSharedPreferences("dsk_preferences",android.content.Context.MODE_PRIVATE)}
 var customerName by remember{mutableStateOf(prefs.getString("customer_name","")?: "")}
 var customerMobile by remember{mutableStateOf(prefs.getString("customer_mobile","")?: "")}
 var favourite by remember{mutableStateOf(prefs.getString("favourite_business","DS LIFESTYLE BRANDS")?: "DS LIFESTYLE BRANDS")}
 var saved by remember{mutableStateOf(false)}
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(13.dp)){
  item{Text(if(hindi) "आपकी प्रोफ़ाइल" else "Your account",fontSize=25.sp,fontWeight=FontWeight.ExtraBold,color=MaterialTheme.colorScheme.primary);Text(if(customerName.isBlank()) if(hindi) "अपना विवरण जोड़कर अनुभव को व्यक्तिगत बनाएं।" else "Add your details for a more personal shopping experience." else if(hindi) "वापसी पर स्वागत है, $customerName" else "Welcome back, $customerName",fontSize=12.sp,color=MaterialTheme.colorScheme.onSurfaceVariant)}
  item{Card(shape=RoundedCornerShape(22.dp)){Column(Modifier.fillMaxWidth().padding(16.dp),verticalArrangement=Arrangement.spacedBy(11.dp)){
   Text(if(hindi) "व्यक्तिगत विवरण" else "Personal details",fontWeight=FontWeight.Bold,fontSize=17.sp)
   OutlinedTextField(customerName,{customerName=it;saved=false},modifier=Modifier.fillMaxWidth(),label={Text(if(hindi) "पूरा नाम" else "Full name")},singleLine=true)
   OutlinedTextField(customerMobile,{customerMobile=it.filter{c->c.isDigit()||c=='+'}.take(13);saved=false},modifier=Modifier.fillMaxWidth(),label={Text(if(hindi) "मोबाइल नंबर" else "Mobile number")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Phone),singleLine=true)
   Button(onClick={prefs.edit().putString("customer_name",customerName.trim()).putString("customer_mobile",customerMobile.trim()).apply();saved=true},modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(13.dp),enabled=customerName.isNotBlank()&&customerMobile.filter{it.isDigit()}.length>=10){Icon(Icons.Default.Save,null);Spacer(Modifier.width(8.dp));Text(if(hindi) "प्रोफ़ाइल सेव करें" else "Save profile")}
   if(saved)Text(if(hindi) "आपकी प्रोफ़ाइल इस डिवाइस पर सेव हो गई है।" else "Your profile has been saved on this device.",fontSize=11.sp,color=androidx.compose.ui.graphics.Color(0xFF16834A))
  }}}
  item{Card(shape=RoundedCornerShape(22.dp),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primary)){Column(Modifier.fillMaxWidth().padding(18.dp),verticalArrangement=Arrangement.spacedBy(5.dp)){Text("DS REWARDS WALLET",color=androidx.compose.ui.graphics.Color(0xFFFFD98A),fontWeight=FontWeight.Bold,fontSize=12.sp);Text("0 Coins",color=androidx.compose.ui.graphics.Color.White,fontSize=29.sp,fontWeight=FontWeight.ExtraBold);Text(if(hindi) "रिवॉर्ड प्रोग्राम सक्रिय होने और ऑर्डर सत्यापित होने पर कॉइन लागू होंगे।" else "Coins will be credited only after verified orders and activation of the rewards programme.",color=androidx.compose.ui.graphics.Color.White,fontSize=11.sp)}}}
  item{Text(if(hindi) "आपकी पसंदीदा श्रेणी" else "Your favourite business",fontWeight=FontWeight.Bold,fontSize=17.sp)}
  item{Column(verticalArrangement=Arrangement.spacedBy(7.dp)){listOf("DS PROPERTIES","DS BUILDING MATERIALS","DS SOLAR TECHNOLOGY","DS SMART GADGETS","DS LIFESTYLE BRANDS").forEach{firm->FilterChip(selected=favourite==firm,onClick={favourite=firm;prefs.edit().putString("favourite_business",firm).apply()},label={Text(firm)},modifier=Modifier.fillMaxWidth())}}}
  item{Card(shape=RoundedCornerShape(18.dp),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){Column(Modifier.fillMaxWidth().padding(15.dp),verticalArrangement=Arrangement.spacedBy(4.dp)){Text(if(hindi) "आपके लिए" else "Picked for you",fontWeight=FontWeight.Bold,color=MaterialTheme.colorScheme.primary);Text("Your ${favourite.removePrefix("DS ")} offers and product updates will be shown here when the live offers service is connected.",fontSize=12.sp)}}}
  item{Divider();Text(if(hindi) "ऐप की भाषा" else "App language",fontWeight=FontWeight.Bold,fontSize=17.sp);Row(horizontalArrangement=Arrangement.spacedBy(10.dp)){FilterChip(selected=!hindi,onClick={onLanguageSelected(false)},label={Text("English")});FilterChip(selected=hindi,onClick={onLanguageSelected(true)},label={Text("हिन्दी")})}}
  item{Text("This profile is stored on this device. Live order history, coin balance, coupons and personalised offers require a connected customer account and admin service.",fontSize=10.sp,color=MaterialTheme.colorScheme.onSurfaceVariant,modifier=Modifier.padding(bottom=12.dp))}
 }
}
