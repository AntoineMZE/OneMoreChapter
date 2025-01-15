package com.example.project

import android.util.Log
import androidx.compose.runtime.remember
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
// import org.jsoup.Jsoup
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.project.APIFiles.Item
import com.example.project.room.BookDao.BookEntity
import kotlinx.coroutines.launch
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(booksViewModel: BooksViewModel) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var lastClickedItem by remember { mutableStateOf<String?>(null) }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController, drawerState, lastClickedItem, currentRoute)
        }
    ) {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    if (currentRoute == AppRoute.LIST.route || currentRoute == AppRoute.COLUMN.route) {
                        if (isSearchVisible) {
                            TextField(
                                value = searchQuery,
                                onValueChange = {
                                    searchQuery = it
                                    booksViewModel.searchBooks(it)
                                },
                                placeholder = { Text("Recherche de Livre ...") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.Transparent
                                )
                            )
                        }else {
                            Text("One More Chapter")
                        }
                    } else {
                        Text("One More Chapter")
                    }
                },
                navigationIcon = {
                    if ((currentRoute?.startsWith(AppRoute.DETAIL.route) == true)
                        || (currentRoute?.startsWith(AppRoute.FAVORITES.route) == true)) {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    } else {
                        IconButton(onClick = {coroutineScope.launch { drawerState.open() }}) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                },
                actions = {
                    if (currentRoute == AppRoute.LIST.route || currentRoute == AppRoute.COLUMN.route) {
                        IconButton(onClick = { isSearchVisible = !isSearchVisible }) {
                            Icon(
                                if (isSearchVisible) Icons.Filled.Close else Icons.Filled.Search,
                                contentDescription = if (isSearchVisible) "Close Search" else "Search"
                            )
                        }
                    }
                }
            )
        },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == AppRoute.LIST.route,
                        onClick = {
                            navController.navigate(AppRoute.LIST.route)
                            {
                                popUpTo(AppRoute.LIST.route) {inclusive = false}
                                launchSingleTop = true
                            }
                        }, icon = {
                            Icon(
                                Icons.AutoMirrored.Filled.List,
                                contentDescription = "Liste"
                            )
                        },
                        label = {Text("Liste")}
                    )
                    NavigationBarItem(
                        selected = currentRoute == AppRoute.COLUMN.route,
                        onClick = {
                            navController.navigate(AppRoute.COLUMN.route){
                                popUpTo(AppRoute.LIST.route){ inclusive = false}
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "Colonne"
                            )
                        },
                        label = {Text("Colonne")}
                    )
                    NavigationBarItem(
                        selected = currentRoute?.startsWith(AppRoute.DETAIL.route) == true,
                        onClick = {
                            if (!lastClickedItem.isNullOrEmpty()) {
                                navController.navigate("${AppRoute.DETAIL.route}/$lastClickedItem")
                            } else {
                                navController.navigate("${AppRoute.DETAIL.route}/")
                            }
                                  },
                        icon = {
                            Icon(
                                Icons.Filled.Info,
                                contentDescription = "Détail"
                            )
                        },
                        label = { Text("Détail") }
                    )
                }
            }
        )
        { innerPadding -> Surface (modifier = Modifier.padding(innerPadding)){
            NavHost(navController, startDestination = AppRoute.LIST.route){
                composable(AppRoute.LIST.route) {
                    ShowAPIBooksInList(navController, onItemClick = { itemId ->
                        if (itemId.isNotEmpty()) {
                            lastClickedItem=itemId
                            navController.navigate("${AppRoute.DETAIL.route}/$itemId")
                        }
                    }, lastClickedItem = lastClickedItem, booksViewModel, "")
                }
                composable(AppRoute.COLUMN.route) {
                    ShowAPIBooksInColumn(navController, onItemClick = { itemId ->
                        if (itemId.isNotEmpty()) {
                            lastClickedItem=itemId
                            navController.navigate("${AppRoute.DETAIL.route}/$itemId")
                        }
                    }, lastClickedItem = lastClickedItem, booksViewModel, "")
                }
                composable("${AppRoute.DETAIL.route}/{itemId}") { backStackEntry ->
                    val itemId = backStackEntry.arguments?.getString("itemId")
                    if (itemId != null) {
                        ShowBookDetail(itemId = itemId, vm = booksViewModel)
                    }
                }
                composable(AppRoute.FAVORITES.route) {
                    ShowFavoritesBooks(booksViewModel, onItemClick = { itemId ->
                        if (itemId.isNotEmpty()) {
                            lastClickedItem=itemId
                            navController.navigate("${AppRoute.DETAIL.route}/$itemId")
                        }
                })
                }
            }
        } }
    }
}

@Composable
fun ShowFavoritesBooks(vm: BooksViewModel, onItemClick: (String) -> Unit) {
    val favoriteBooks by vm.getFavoriteBooks().collectAsState(initial = emptyList())
    if (favoriteBooks.isEmpty()) {
        Text(
            text = "Rien à afficher pour l'instant",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
    } else {
        LazyColumn {
            items(favoriteBooks) { book ->
                val isFavorite = favoriteBooks.any { it.id == book.id }

                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable{onItemClick(book.id) },
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        DisplayBookPhoto(
                            photoUrl = book.imageUrl,
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            DisplayBookTitle(
                                title = book.title
                            )

                            DisplayBookAuthors(
                                authors = book.authors.split(", "),
                            )

                        }

                    }
                    IconButton(onClick = {
                        val updatedBook = book.copy(isFavorite = !isFavorite)
                        vm.toggleFavorite(updatedBook)
                    }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favoris",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerContent(navController: NavController, drawerState: DrawerState, lastClickedItem: String?, currentRoute: String?) {
    val coroutineScope = rememberCoroutineScope();
    ModalDrawerSheet {
        Column (modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top){
            Text(
                text = "One More Chapter",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
            NavigationDrawerItem(
                label = { Text("Favoris")},
                selected = currentRoute == AppRoute.FAVORITES.route,
                onClick = {
                    navController.navigate(AppRoute.FAVORITES.route) {
                        launchSingleTop = true
                    }
                    coroutineScope.launch { drawerState.close()
                    }

                }
            )
        }
    }
}

@Composable
fun ShowAPIBooksInList(navController: NavController,
                       onItemClick: (String) -> Unit,
                       lastClickedItem: String? = null,
                       vm : BooksViewModel,
                       query: String
) {

    val favoriteBooks by vm.getFavoriteBooks().collectAsState(initial = emptyList())
    LazyColumn(Modifier.padding(16.dp)) {
        items(vm.booksList.value.items) { item ->
            val isFavorite = favoriteBooks.any { it.id == item.id }
            val book = item.id.let {
                item.volumeInfo?.title?.let { it1 ->
                    item.volumeInfo.authors?.let { it2 ->
                        item.volumeInfo.imageLinks?.let { it3 ->
                            BookEntity(
                                id = it,
                                title = it1,
                                authors = it2.joinToString(", "),
                                imageUrl = it3.thumbnail
                            )
                        }
                    }
                }
            }
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(8.dp)
                    .clickable {
                        item.id.let {  Log.d("SelectedItem", "Item: ${item.volumeInfo?.title}, ID: ${item.id}")
                            onItemClick(item.id) } // Passe l'ID à onItemClick
                    },

                colors = CardDefaults.cardColors(containerColor = Color.LightGray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    item.volumeInfo?.imageLinks?.let {
                        DisplayBookPhoto(
                            photoUrl = it.thumbnail,
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        item.volumeInfo?.title?.let {
                            DisplayBookTitle(
                                title = it
                            )
                        }

                        item.volumeInfo?.authors?.let {
                            DisplayBookAuthors(
                                authors = it,
                            )
                        }
                    }


                }
                IconButton(onClick = {
                    val updatedBook = book?.copy(isFavorite = !isFavorite)
                    if (updatedBook != null) {
                        vm.toggleFavorite(updatedBook)
                    }
                }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favoris",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
            }
        }

    }
}

@Composable
fun ShowAPIBooksInColumn(
    navController: NavController,
    onItemClick: (String) -> Unit,
    lastClickedItem: String? = null,
    vm: BooksViewModel,
    query : String
)

{
    val favoriteBooks by vm.getFavoriteBooks().collectAsState(initial = emptyList())
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // pour avoir deux cologne dans chaque ligne
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(vm.booksList.value.items) {item ->
            val isFavorite = favoriteBooks.any { it.id == item.id }
            val book = item.id.let {
                item.volumeInfo?.title?.let { it1 ->
                    item.volumeInfo.authors?.let { it2 ->
                        item.volumeInfo.imageLinks?.let { it3 ->
                            BookEntity(
                                id = it,
                                title = it1,
                                authors = it2.joinToString(", "),
                                imageUrl = it3.thumbnail
                            )
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        item.id?.let {  Log.d("SelectedItem", "Item: ${item.volumeInfo?.title}, ID: ${item.id}")
                            onItemClick(item.id) } // Passe l'ID à onItemClick
                    },
                colors = CardDefaults.cardColors(containerColor = Color.LightGray)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    item.volumeInfo?.imageLinks?.let {
                        DisplayBookPhoto(
                            photoUrl = it.thumbnail,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    item.volumeInfo?.title?.let {
                        DisplayBookTitle(
                            title = it
                        )
                    }
                    item.volumeInfo?.authors?.let {
                        DisplayBookAuthors(
                            authors = it,
                        )
                    }
                    IconButton(onClick = {
                        val updatedBook = book?.copy(isFavorite = !isFavorite)
                        if (updatedBook != null) {
                            vm.toggleFavorite(updatedBook)
                        }
                    }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favoris",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }

                }
            }
        }
    }

}

@Composable
fun DisplayBookPhoto(photoUrl: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = convertToHttps(photoUrl),
        contentDescription = null,
        modifier = modifier
            .height(150.dp)
    )
}

@Composable
fun DisplayBookTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        fontSize = 16.sp,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}

@Composable
fun DisplayBookAuthors(authors: List<String>, modifier: Modifier = Modifier) {
    Text(
        text = authors.joinToString(", "),
        fontSize = 10.sp,
        color = Color.Gray,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
fun ShowBookDetail(itemId: String, vm: BooksViewModel) {

    if (itemId.isBlank()) {
        Text(
            text = "Aucun livre sélectionné.",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        return
    }

    var itemDetail by remember { mutableStateOf<Item?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(itemId) {
        try {
            val response = vm.detailsBooks(itemId)
            itemDetail = response
        } catch (e: Exception) {
            errorMessage = "Erreur lors du chargement des détails: ${e.message}"
        }
        isLoading = false
    }

    when {
        isLoading -> {
            Text("Chargement en cours...", modifier = Modifier.padding(16.dp))
        }
        errorMessage.isNotEmpty() -> {
            Text(errorMessage, color = Color.Red, modifier = Modifier.padding(16.dp))
        }
        itemDetail == null -> {
            Text("Aucun détail disponible.", modifier = Modifier.padding(16.dp))
        }
        else -> {
            DisplayBookDetails(item = itemDetail!!)
        }
    }
}

@Composable
fun DisplayBookDetails(item: Item) {
    val volumeInfo = item.volumeInfo

    val title = volumeInfo?.title
    val authors = volumeInfo?.authors?.joinToString(", ")
    val publisher = volumeInfo?.publisher
    val pubDate = volumeInfo?.publishedDate
    val pageCount = volumeInfo?.pageCount
    val language = volumeInfo?.language
    val description = volumeInfo?.description
    val categories = volumeInfo?.categories?.joinToString(", ")


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        volumeInfo?.imageLinks?.thumbnail?.let { imageUrl ->
            DisplayBookPhoto(imageUrl)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!title.isNullOrBlank()) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
        }

        if (!authors.isNullOrBlank()) {
            Text(
                text = "Auteur(s) : $authors",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (!publisher.isNullOrBlank()) {
            Text(
                text = "Éditeur : $publisher",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (!pubDate.isNullOrBlank()) {
            Text(
                text = "Date de publication : $pubDate",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (pageCount != null && pageCount > 0) {
            Text(
                text = "Nombre de pages : $pageCount",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (!language.isNullOrBlank()) {
            Text(
                text = "Langue : $language",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (!categories.isNullOrBlank()) {
            Text(
                text = "Catégorie(s) : $categories",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        if (!description.isNullOrBlank()) {
            Text(
                text = "Description :",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = cleanHtml(description),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
        }
    }
}

fun cleanHtml(html: String): String {
    return HtmlCompat.fromHtml(html, FROM_HTML_MODE_LEGACY).toString() // Convertir le HTML en texte brut
}

fun convertToHttps(url: String): String {
    return url.replace("http://", "https://")
}