window.onload = function() {
    const clipboard = new ClipboardJS('.btn');
    clipboard.on('success', function(e) {
        e.clearSelection();
        e.trigger.textContent = 'Copied!';
    });
};
