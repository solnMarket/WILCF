function toggleFeedbackSelection(id) {
    var checkbox = document.getElementById('checkbox-' + id);
    var isSelectedInput = document.getElementById('isSelected-' + id);
    isSelectedInput.value = checkbox.checked;
}
