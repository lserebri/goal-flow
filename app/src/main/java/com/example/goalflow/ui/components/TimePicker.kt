import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlin.collections.toList

@Preview(showBackground = true, apiLevel = 35)
@Composable
fun TimePickerDialogPreview() {
	TimePickerDialog(
		initialHour = 0,
		initialMinute = 0,
		onDismiss = {  },
		onConfirm = { _, _ ->  }
	)
}

@Composable
private fun TrackTimeWheelSelection(
	listState: LazyListState,
	values: List<Int>,
	onSelectionChanged: (Int) -> Unit
) {
	LaunchedEffect(listState) {
		snapshotFlow { listState.layoutInfo }
			.collect { layoutInfo ->
				val center = layoutInfo.viewportStartOffset +
						(layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2

				val centerItem = layoutInfo.visibleItemsInfo.minByOrNull { item ->
					kotlin.math.abs((item.offset + item.size / 2) - center)
				}

				centerItem?.let {
					onSelectionChanged(values[it.index % values.size])
				}
			}
	}
}

@Composable
fun createTimeWheelState(
	values: List<Int>,
	initialValue: Int,
	repeatCount: Int,
	rowHeightDp: Dp = 60.dp,
): LazyListState {
	val density = LocalDensity.current

	val initialIndex = values.size * repeatCount / 2 + initialValue

	val halfRowHeightPx = with(density) { (rowHeightDp / 2).roundToPx() }

	return rememberLazyListState(
		initialFirstVisibleItemIndex = initialIndex,
		initialFirstVisibleItemScrollOffset = halfRowHeightPx
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
	initialHour: Int = 0,
	initialMinute: Int = 0,
	repeatCount: Int = 20,
	onDismiss: () -> Unit,
	onConfirm: (Int, Int) -> Unit
) {
	val hours = (0..23).toList()
	val minutes = (0..59).toList()


	val totalHours = List(hours.size * repeatCount) { hours[it % hours.size] }
	val totalMinutes = List(minutes.size * repeatCount) { minutes[it % minutes.size] }

	val hourListState = createTimeWheelState(hours, initialHour, repeatCount)
	val minuteListState = createTimeWheelState(minutes, initialMinute, repeatCount)


	val selectedHour = remember { mutableIntStateOf(initialHour) }
	val selectedMinute = remember { mutableIntStateOf(initialMinute) }

	TrackTimeWheelSelection(
		listState = hourListState,
		values = totalHours,
		onSelectionChanged = { selectedHour.intValue = it }
	)

	TrackTimeWheelSelection(
		listState = minuteListState,
		values = totalMinutes,
		onSelectionChanged = { selectedMinute.intValue = it }
	)

	Dialog(onDismissRequest = onDismiss) {
		Surface(
			modifier = Modifier
				.width(280.dp)
				.wrapContentHeight(),
			tonalElevation = 6.dp
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth(),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Row(
					modifier = Modifier.fillMaxWidth(),
				) {
					Column(
						modifier = Modifier.weight(1f),
						horizontalAlignment = Alignment.CenterHorizontally,
					) {
						TextButton(onClick = onDismiss) {
							Text("H", fontSize = 18.sp)
						}
					}
					Column(
						modifier = Modifier.weight(1f),
						horizontalAlignment = Alignment.CenterHorizontally,
					) {
						TextButton(onClick = {
							onConfirm(selectedHour.intValue, selectedMinute.intValue)
						}) {
							Text("M", fontSize = 18.sp)
						}
					}
				}

//				Spacer(modifier = Modifier.height(16.dp))

				Row(
					modifier = Modifier
						.fillMaxWidth(),
					horizontalArrangement = Arrangement.Center,
					verticalAlignment = Alignment.CenterVertically
				) {
					TimeColumn(
						values = totalHours,
						lazyListState = hourListState,
						selectedValue = selectedHour.intValue,
						modifier = Modifier.weight(1f)
					)
					TimeColumn(
						values = totalMinutes,
						lazyListState = minuteListState,
						selectedValue = selectedMinute.intValue,
						modifier = Modifier.weight(1f)
					)
				}

//				Spacer(modifier = Modifier.height(24.dp))

				Row(
					modifier = Modifier.fillMaxWidth(),
				) {
					Column(
						modifier = Modifier.weight(1f),
						horizontalAlignment = Alignment.CenterHorizontally,
					) {
						TextButton(onClick = onDismiss) {
							Text("Cancel", fontSize = 18.sp)
						}
					}
					Column(
						modifier = Modifier.weight(1f),
						horizontalAlignment = Alignment.CenterHorizontally,
					) {
						TextButton(onClick = {
							onConfirm(selectedHour.intValue, selectedMinute.intValue)
						}) {
							Text("OK", fontSize = 18.sp)
						}
					}
				}
			}
		}
	}
}

@Composable
private fun TimeColumn(
	values: List<Int>,
	lazyListState: LazyListState,
	selectedValue: Int,
	modifier: Modifier = Modifier
) {
	val rowHeight = 60.dp
	val coroutineScope = rememberCoroutineScope()

	Box(modifier = modifier.height(rowHeight * 3)) {
		LazyColumn(
			state = lazyListState,
			contentPadding = PaddingValues(
				top = rowHeight / 2,
				bottom = rowHeight / 2
			),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
			flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState),
			modifier = Modifier.fillMaxSize()
		) {
			itemsIndexed(values) { _, value ->
				val isSelected = value == selectedValue

				val animatedFontSize by animateFloatAsState(
					targetValue = if (isSelected) 24f else 18f,
					label = "FontSizeAnim"
				)

				val animatedColor by animateColorAsState(
					targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
					label = "ColorAnim"
				)

				Box(
					modifier = Modifier
						.fillMaxWidth()
						.height(rowHeight),
					contentAlignment = Alignment.Center
				) {
					Text(
						text = "%02d".format(value),
						fontSize = animatedFontSize.sp,
						color = animatedColor,
						textAlign = TextAlign.Center
					)
				}
			}
		}

		Box(
			modifier = Modifier
				.align(Alignment.Center)
				.fillMaxWidth()
				.height(rowHeight)
				.border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
		)
	}
}
