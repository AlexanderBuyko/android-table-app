package com.demonstration.baseui.widgets.widgets.models

import androidx.annotation.ColorRes
import com.example.baseui.R

sealed class Workspace(
    open val name: String,
    open val positions: Array<Position>
) {

    @ColorRes
    abstract fun getColorRes(): Int

    fun seatsAmount() = positions.size
}

data class AvailableWorkspace(
    override val name: String,
    override val positions: Array<Position>,
) : Workspace(name, positions) {
    override fun getColorRes(): Int {
        return R.color.mercury
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AvailableWorkspace

        if (name != other.name) return false
        if (!positions.contentEquals(other.positions)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + positions.contentHashCode()
        return result
    }
}

data class ReservedWorkspace(
    override val name: String,
    override val positions: Array<Position>,
) : Workspace(name, positions) {

    override fun getColorRes(): Int {
        return R.color.nobel
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReservedWorkspace

        if (name != other.name) return false
        if (!positions.contentEquals(other.positions)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + positions.contentHashCode()
        return result
    }
}

data class SelectedWorkspace(
    override val name: String,
    override val positions: Array<Position>
) : Workspace(name, positions) {
    override fun getColorRes(): Int {
        return R.color.cod_gray
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SelectedWorkspace

        if (name != other.name) return false
        if (!positions.contentEquals(other.positions)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + positions.contentHashCode()
        return result
    }
}

data class Position(
    val row: Int,
    val column: Int
) {

    fun isValid(segmentCount: Int) = row in 0 until segmentCount && column  in 0 until segmentCount
}