package com.demonstration.baseui.widgets.extentions

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.collection.SparseArrayCompat
import androidx.collection.keyIterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import java.lang.reflect.Field
import java.util.*

private val actionsField: Field by lazy {
    NavDestination::class.java.getDeclaredField("mActions").also { field ->
        field.isAccessible = true
    }
}

fun FragmentActivity.findNavControllerFromFragment(fragmentId: Int): NavController {
    return (supportFragmentManager.findFragmentById(fragmentId) as NavHostFragment)
        .navController
}

fun Fragment.findNavControllerSafe(): NavController? =
    kotlin.runCatching { findNavController() }.getOrNull()

private fun doesDestinationRequireArguments(searchedDestinationId: Int, graph: NavGraph): Boolean =
    graph[searchedDestinationId].arguments.values.any { !it.isNullable && !it.isDefaultValuePresent }

private fun constructPath(
    searchedDestination: NavDestination,
    previousDestinationMap: HashMap<NavDestination, NavDestination>
): LinkedList<NavActionEntry> {
    val path = LinkedList<NavActionEntry>()
    var current = searchedDestination
    var parent = previousDestinationMap[current]
    while (parent != null) {
        val parentActions = parent.getActions(actionsField)
        val fromParentToCurrentActionEntry = parentActions.keyIterator()
            .asSequence()
            .map { actionId -> NavActionEntry(actionId, parentActions[actionId]!!) }
            .first { it.action.destinationId == current.id }
        path.addFirst(fromParentToCurrentActionEntry)
        current = parent
        parent = previousDestinationMap[current]
    }
    return path
}

data class NavActionEntry(
    @IdRes val actionId: Int,
    val action: NavAction
)

@Suppress("UNCHECKED_CAST")
private fun NavDestination.getActions(
    actionsField: Field
): SparseArrayCompat<NavAction> =
    (actionsField.get(this) as? SparseArrayCompat<NavAction>) ?: SparseArrayCompat()

private fun NavGraph.getDestinations(): Set<NavDestination> = iterator().asSequence().toSet()

/**
 * Safe call for [NavController.navigate(NavDirections, NavOptions?)][NavController.navigate]
 * Prevents navigation for Destination if it does not contain the required ActionId
 */
@Suppress("NOTHING_TO_INLINE")
inline fun NavDirections.navigateWith(navController: NavController, navOptions: NavOptions? = null) {
    if (navController.currentDestination?.getAction(actionId) != null) {
        navController.navigate(this, navOptions)
    }
}
/**
 * Safe call for [NavController.navigate(NavDirections, Navigator.Extras)][NavController.navigate]
 * Prevents navigation for Destination if it does not contain the required ActionId
 */
@Suppress("NOTHING_TO_INLINE")
inline fun NavDirections.navigateWith(navController: NavController, navigatorExtras: Navigator.Extras) {
    if (navController.currentDestination?.getAction(actionId) != null) {
        navController.navigate(this, navigatorExtras)
    }
}

/**
 * Safe call for [NavController.navigate(@IdRes int resId)][NavController.navigate]
 * Prevents navigation for Destination if it does not contain the required ActionId
 */
@Suppress("NOTHING_TO_INLINE")
inline fun NavController.navigateSafe(@IdRes resId: Int) {
    if (currentDestination?.getAction(resId) != null) {
        navigate(resId)
    }
}

/**
 * Safe call for [NavController.navigate(@IdRes int resId, @Nullable Bundle args)][NavController.navigate]
 * Prevents navigation for Destination if it does not contain the required ActionId
 */
@Suppress("NOTHING_TO_INLINE")
inline fun NavController.navigateSafe(@IdRes resId: Int, args: Bundle) {
    if (currentDestination?.getAction(resId) != null) {
        navigate(resId, args)
    }
}

/**
 * Check whether we have destination in back stack or not
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun NavController.hasInBackStack(@IdRes destinationId: Int) = kotlin.runCatching {
    getBackStackEntry(destinationId)
}.isSuccess