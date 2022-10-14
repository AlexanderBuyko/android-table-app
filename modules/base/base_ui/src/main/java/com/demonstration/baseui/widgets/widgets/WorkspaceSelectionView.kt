package com.demonstration.baseui.widgets.widgets

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toRectF
import com.demonstration.baseui.widgets.extentions.px
import com.demonstration.baseui.widgets.widgets.WorkspaceSelectionView.Companion.SEGMENTS_COUNT
import com.demonstration.baseui.widgets.widgets.models.*
import com.example.baseui.R
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.roundToInt


class WorkspaceSelectionView : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    private val minSize = resources.getDimension(R.dimen.workspace_selection_min_size).toInt()
    private val segmentMargin = 4.px

    private val globalRect = Rect()
    private val innerRect = Rect()
    private val segmentSize: Int
        get() = innerRect.width() / SEGMENTS_COUNT
    private val cornerRect = Rect()

    private val startPoint = Point()
    private val secondLinePoint = Point()
    private val segmentRect = Rect()

    private var direction = Direction.RIGHT
    private lateinit var position: Position

    private val segmentPath = Path()
    private val workspacePath = Path()

    private val windSandPaint = Paint().apply {
        color = getColor(R.color.wild_sand)
        style = Paint.Style.FILL_AND_STROKE
    }
    private val workspacePaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
    }
    private val textPaint = Paint().apply {
        color = getColor(R.color.nobel)
        style = Paint.Style.FILL_AND_STROKE
        typeface = ResourcesCompat.getFont(context, R.font.rational_regular)
        textSize = 16.px.toFloat()
        textAlign = Paint.Align.CENTER
    }

    private var selectedWorkspaceColor: Int? = null
    private var selectedTextAlpha: Int? = null

    private var previousSelectedWorkspaceName: String? = null
    private var previousSelectedWorkspaceColor: Int? = null
    private var previousSelectedTextAlpha: Int? = null

    private var workspaces: List<Workspace>? = null
    private var callback: Callback? = null

    private val strategy = MovementStrategy()

    private val generalGestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onSingleTapUp(event: MotionEvent): Boolean {
                return handleOnSingleTapUp(event)
            }
        })

    fun setWorkspaces(workspaces: List<Workspace>) {
        this.workspaces = workspaces
    }

    fun setCallback(callback: Callback?) {
        this.callback = callback
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return SavedState(workspaces, superState)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            workspaces = state.workspaces
            super.onRestoreInstanceState(state.superState)
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return generalGestureDetector.onTouchEvent(event)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = measureViewSize(widthMeasureSpec)
        val viewHeight = measureViewSize(heightMeasureSpec)
        max(viewWidth, viewHeight).also { viewSize ->
            setMeasuredDimension(viewSize, viewSize)
        }
    }

    private fun measureViewSize(measureSpec: Int): Int {
        var result = minSize
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        when (specMode) {
            MeasureSpec.UNSPECIFIED -> result = minSize
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> result = max(minSize, specSize)
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.getClipBounds(globalRect)
        globalRect.also { rect ->
            innerRect.set(rect)
            innerRect.inset(8.px, 8.px)
        }

        drawBackground(canvas)
        drawWorkspaces(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        calculateSegmentWithRadius(globalRect, 16.px)
        canvas.drawPath(segmentPath, windSandPaint)
    }

    private fun drawWorkspaces(canvas: Canvas) {
        workspaces?.let {
            for (workspace in it) {
                calculateWorkspacePath(workspace.positions, 16.px)

                workspacePaint.color = getColor(workspace.getColorRes())
                textPaint.alpha = if (workspace is SelectedWorkspace) 255 else 0
                if (workspace is SelectedWorkspace) {
                    selectedWorkspaceColor?.let { workspacePaint.color = it }
                    selectedTextAlpha?.let { textPaint.alpha = it }
                }

                if (workspace is AvailableWorkspace &&
                    workspace.name == previousSelectedWorkspaceName
                ) {
                    previousSelectedWorkspaceColor?.let { workspacePaint.color = it }
                    previousSelectedTextAlpha?.let { textPaint.alpha = it }
                }

                canvas.drawPath(workspacePath, workspacePaint)

                val xPos = segmentRect.centerX().toFloat()
                val yPos = segmentRect.centerY() - (textPaint.descent() + textPaint.ascent()) / 2
                canvas.drawText(workspace.name, xPos, yPos, textPaint)
            }
        }
    }

    private fun calculateSegmentWithRadius(rect: Rect, radius: Int) {
        segmentPath.apply {
            reset()
            moveTo(rect.left.toFloat() + radius, rect.top.toFloat())
            lineTo(rect.right.toFloat() - radius, rect.top.toFloat())
            cornerRect.set(rect.right - radius, rect.top, rect.right, rect.top + radius)
            arcTo(cornerRect.toRectF(), 270f, 90f)
            lineTo(rect.right.toFloat(), rect.bottom.toFloat() - radius)
            cornerRect.set(rect.right - radius, rect.bottom - radius, rect.right, rect.bottom)
            arcTo(cornerRect.toRectF(), 0f, 90f)
            lineTo(rect.left.toFloat() + radius, rect.bottom.toFloat())
            cornerRect.set(rect.left, rect.bottom - radius, rect.left + radius, rect.bottom)
            arcTo(cornerRect.toRectF(), 90f, 90f)
            lineTo(rect.left.toFloat(), rect.top.toFloat() + radius)
            cornerRect.set(rect.left, rect.top, rect.left + radius, rect.top + radius)
            arcTo(cornerRect.toRectF(), 180f, 90f)
            lineTo(rect.left.toFloat() + radius, rect.top.toFloat())
        }
    }

    private fun calculateWorkspacePath(positions: Array<Position>, radius: Int) {
        workspacePath.apply {
            reset()
            direction = Direction.RIGHT
            position = findStartPosition(positions)
            setupSegmentRect(position)
            startPoint.set(segmentRect.left + radius, segmentRect.top)
            moveTo(startPoint.x.toFloat(), startPoint.y.toFloat())

            while (true) {
                updateSecondLinePoint()
                drawLineAccordingToDirection(radius)


                val (newDirection, newPosition) =
                    strategy.nextMove(direction, position, positions)
                if (newDirection != direction) {
                    drawArcAccordingToNewDirection(newDirection, radius)
                }

                direction = newDirection
                position = newPosition
                setupSegmentRect(position)

                val pathMeasure = PathMeasure(this, false)
                val lastPointCoordinates = floatArrayOf(0f, 0f)
                pathMeasure.getPosTan(pathMeasure.length, lastPointCoordinates, null)

                if (hypot(
                        (lastPointCoordinates[0] - startPoint.x).toDouble(),
                        (lastPointCoordinates[1] - startPoint.y).toDouble()
                    ).roundToInt() <= radius / 2
                ) {
                    lineTo(startPoint.x.toFloat(), startPoint.y.toFloat())
                    break
                }
            }
        }
    }

    private fun findStartPosition(positions: Array<Position>): Position {
        return positions.sortedWith { o1, o2 ->
            return@sortedWith if (o1.column - o2.column == 0)
                o1.row - o2.row else o1.column - o2.column
        }.first()
    }

    private fun setupSegmentRect(position: Position) {
        setupSegmentRect(position.row, position.column)
    }

    private fun setupSegmentRect(row: Int, column: Int) {
        segmentRect.set(
            row * segmentSize,
            column * segmentSize,
            row.inc() * segmentSize,
            column.inc() * segmentSize
        )
        segmentRect.offset(innerRect.left, innerRect.top)
        segmentRect.inset(segmentMargin, segmentMargin)
    }

    private fun updateSecondLinePoint() {
        when (direction) {
            Direction.RIGHT -> secondLinePoint.set(segmentRect.right, segmentRect.top)
            Direction.TOP -> secondLinePoint.set(segmentRect.left, segmentRect.top)
            Direction.LEFT -> secondLinePoint.set(segmentRect.left, segmentRect.bottom)
            Direction.BOTTOM -> secondLinePoint.set(segmentRect.right, segmentRect.bottom)
        }
    }

    private fun Path.drawLineAccordingToDirection(radius: Int) {
        when (direction) {
            Direction.RIGHT -> lineTo(
                secondLinePoint.x.toFloat() - radius,
                secondLinePoint.y.toFloat()
            )
            Direction.TOP -> lineTo(
                secondLinePoint.x.toFloat(),
                secondLinePoint.y.toFloat() + radius
            )
            Direction.LEFT -> lineTo(
                secondLinePoint.x.toFloat() + radius,
                secondLinePoint.y.toFloat()
            )
            Direction.BOTTOM -> lineTo(
                secondLinePoint.x.toFloat(),
                secondLinePoint.y.toFloat() - radius
            )
        }
    }

    private fun Path.drawArcAccordingToNewDirection(
        newDirection: Direction,
        radius: Int
    ) {
        when (newDirection) {
            Direction.RIGHT -> {
                if (direction == Direction.TOP) {
                    cornerRect.set(
                        segmentRect.left,
                        segmentRect.top,
                        segmentRect.left + radius,
                        segmentRect.top + radius
                    )
                    arcTo(cornerRect.toRectF(), 180f, 90f)
                } else {
                    cornerRect.set(
                        segmentRect.right,
                        segmentRect.bottom - radius,
                        segmentRect.right + radius,
                        segmentRect.bottom
                    )
                    arcTo(cornerRect.toRectF(), 180f, -90f)
                }
            }
            Direction.TOP -> {
                if (direction == Direction.LEFT) {
                    cornerRect.set(
                        segmentRect.left,
                        segmentRect.bottom - radius,
                        segmentRect.left + radius,
                        segmentRect.bottom
                    )
                    arcTo(cornerRect.toRectF(), 90f, 90f)
                } else {
                    cornerRect.set(
                        segmentRect.right - radius,
                        segmentRect.bottom - radius,
                        segmentRect.right,
                        segmentRect.bottom
                    )
                    arcTo(cornerRect.toRectF(), 90f, -90f)
                }
            }
            Direction.LEFT -> {
                if (direction == Direction.BOTTOM) {
                    cornerRect.set(
                        segmentRect.right - radius,
                        segmentRect.bottom - radius,
                        segmentRect.right,
                        segmentRect.bottom
                    )
                    arcTo(cornerRect.toRectF(), 0f, 90f)
                } else {
                    cornerRect.set(
                        segmentRect.right - radius,
                        segmentRect.top,
                        segmentRect.right,
                        segmentRect.top + radius
                    )
                    arcTo(cornerRect.toRectF(), 0f, -90f)
                }
            }
            Direction.BOTTOM -> {
                if (direction == Direction.RIGHT) {
                    cornerRect.set(
                        segmentRect.right - radius,
                        segmentRect.top,
                        segmentRect.right,
                        segmentRect.top + radius
                    )
                    arcTo(cornerRect.toRectF(), 270f, 90f)
                } else {
                    cornerRect.set(
                        segmentRect.left,
                        segmentRect.top,
                        segmentRect.left + radius,
                        segmentRect.top + radius
                    )
                    arcTo(cornerRect.toRectF(), 270f, -90f)
                }
            }
        }
    }

    private fun handleOnSingleTapUp(event: MotionEvent): Boolean {
        workspaces?.map { workspace ->
            when (workspace) {
                is SelectedWorkspace -> {
                    /*
                    *  Remove selection
                    * */
                    previousSelectedWorkspaceName = workspace.name
                    startColorChangeAnimation(onSelection = false)
                    AvailableWorkspace(workspace.name, workspace.positions)
                }
                is AvailableWorkspace -> {
                    /*
                    * Add selection
                    * */
                    val containsTouch = workspace.positions
                        .map { position ->
                            setupSegmentRect(position)
                            segmentRect.contains(event.x.toInt(), event.y.toInt())
                        }
                        .contains(true)
                    if (containsTouch) {
                        startColorChangeAnimation(onSelection = true)
                        SelectedWorkspace(workspace.name, workspace.positions)
                    } else {
                        workspace
                    }
                }
                is ReservedWorkspace -> {
                    /*
                    * Should be ignored
                    * */
                    workspace
                }
            }
        }
            .also { workspaces = it }
            .also {
                val selectedWorkspace =
                    workspaces?.filterIsInstance<SelectedWorkspace>()?.firstOrNull()
                if (selectedWorkspace != null) {
                    callback?.onSeatSelected(selectedWorkspace)
                } else {
                    callback?.onSeatReleased()
                }
            }
        invalidate()
        return true
    }

    private fun startColorChangeAnimation(onSelection: Boolean) {
        val colorAnimator = if (onSelection) {
            ValueAnimator.ofArgb(getColor(R.color.mercury), getColor(R.color.cod_gray))
        } else {
            ValueAnimator.ofArgb(getColor(R.color.cod_gray), getColor(R.color.mercury))
        }
        colorAnimator.addUpdateListener {
            val value = it.animatedValue as Int
            if (onSelection) {
                selectedWorkspaceColor = value
            } else {
                previousSelectedWorkspaceColor = value
            }
            invalidate()
        }

        val alphaAnimator = if (onSelection) {
            ValueAnimator.ofInt(0, 255)
        } else {
            ValueAnimator.ofInt(255, 0)
        }
        alphaAnimator.addUpdateListener {
            val value = it.animatedValue as Int
            if (onSelection) {
                selectedTextAlpha = value
            } else {
                previousSelectedTextAlpha = value
            }
            invalidate()
        }

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(colorAnimator, alphaAnimator)
        animatorSet.doOnEnd {
            selectedWorkspaceColor = null
            selectedTextAlpha = null

            previousSelectedWorkspaceColor = null
            previousSelectedWorkspaceColor = null

            previousSelectedWorkspaceName = null
        }
        animatorSet.start()
    }

    private fun getColor(@ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(context, colorRes)
    }


    companion object {
        const val SEGMENTS_COUNT = 5
    }

    interface Callback {
        fun onSeatSelected(workspace: Workspace)
        fun onSeatReleased()
    }

    internal class SavedState : BaseSavedState {

        var workspaces: List<Workspace>?

        constructor(workspaces: List<Workspace>?, superState: Parcelable?) : super(superState) {
            this.workspaces = workspaces
        }

        private constructor(input: Parcel) : super(input) {
            workspaces = listOf()
            input.readList(workspaces!!, List::class.java.classLoader)
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeList(workspaces)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}

enum class Direction {
    RIGHT, TOP, LEFT, BOTTOM
}

class MovementStrategy {

    fun nextMove(
        currentDirection: Direction,
        currentPosition: Position,
        positions: Array<Position>
    ): Pair<Direction, Position> {
        val checkDirections = getCheckDirection(currentDirection)
        for ((index, checkDirection) in checkDirections.withIndex()) {
            getPositionToCheck(
                currentPosition = currentPosition,
                currentDirection = currentDirection,
                checkDirection = checkDirection
            )?.let {
                if (positions.contains(it)) {
                    return if (index == 0) {
                        Pair(checkDirection, it)
                    } else {
                        Pair(currentDirection, it)
                    }
                }
            }
        }
        return Pair(nextToCurrent(currentDirection), currentPosition)
    }

    private fun getPositionToCheck(
        currentPosition: Position,
        currentDirection: Direction,
        checkDirection: Direction
    ): Position? {
        when (currentDirection) {
            Direction.RIGHT -> {
                if (checkDirection == Direction.TOP) {
                    currentPosition.copy(
                        row = currentPosition.row.inc(),
                        column = currentPosition.column.dec()
                    )
                } else {
                    currentPosition.copy(row = currentPosition.row.inc())
                }.also { position ->
                    return if (position.isValid(SEGMENTS_COUNT)) position else null
                }
            }
            Direction.TOP -> {
                if (checkDirection == Direction.LEFT) {
                    currentPosition.copy(
                        row = currentPosition.row.dec(),
                        column = currentPosition.column.dec()
                    )
                } else {
                    currentPosition.copy(column = currentPosition.column.dec())
                }.also { position ->
                    return if (position.isValid(SEGMENTS_COUNT)) position else null
                }
            }
            Direction.LEFT -> {
                if (checkDirection == Direction.BOTTOM) {
                    currentPosition.copy(
                        row = currentPosition.row.dec(),
                        column = currentPosition.column.inc()
                    )
                } else {
                    currentPosition.copy(row = currentPosition.row.dec())
                }.also { position ->
                    return if (position.isValid(SEGMENTS_COUNT)) position else null
                }
            }
            Direction.BOTTOM -> {
                if (checkDirection == Direction.RIGHT) {
                    currentPosition.copy(
                        row = currentPosition.row.inc(),
                        column = currentPosition.column.inc()
                    )
                } else {
                    currentPosition.copy(column = currentPosition.column.inc())
                }.also { position ->
                    return if (position.isValid(SEGMENTS_COUNT)) position else null
                }
            }
        }
    }

    private fun getCheckDirection(currentDirection: Direction): Array<Direction> {
        return when (currentDirection) {
            Direction.RIGHT -> arrayOf(Direction.TOP, Direction.BOTTOM)
            Direction.TOP -> arrayOf(Direction.LEFT, Direction.RIGHT)
            Direction.LEFT -> arrayOf(Direction.BOTTOM, Direction.TOP)
            Direction.BOTTOM -> arrayOf(Direction.RIGHT, Direction.LEFT)
        }
    }

    private fun nextToCurrent(currentDirection: Direction): Direction {
        return when (currentDirection) {
            Direction.RIGHT -> Direction.BOTTOM
            Direction.TOP -> Direction.RIGHT
            Direction.LEFT -> Direction.TOP
            Direction.BOTTOM -> Direction.LEFT
        }
    }
}