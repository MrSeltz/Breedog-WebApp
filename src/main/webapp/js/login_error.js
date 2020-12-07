function preparePage(page, callback) {
    // Append error modal  
    $('body').prepend(`
        <div class="modal fade" id="auth-error-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
            aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header text-center">
                        <h5 class="modal-title col-12" id="exampleModalLongTitle">Login Error</h5>
                    </div>
                    <div class="modal-body text-center">
                        <p>Authentication Error - Login Required.<br> Click OK and you will be redirected to the homepage.
                        </p>
                    </div>
                    <div class="modal-footer">
                        <a type="button" id="back-home-btn" class="btn" href="../index.html">OK</a>
                    </div>
                </div>
            </div>
        </div>`);

    // Get user info
    const info = getUserInfo();

    // Check if there is a valid token
    if (info.username == null && info.breederfc == null) {
        console.log("Show modal");
        // Let modal not closable if click outside
        $('#auth-error-modal').modal({ backdrop: 'static', keyboard: false });
        // Set modal background color
        $("#auth-error-modal").css('background', '#BED3CD');
        // Set button background color
        $('#back-home-btn').css('background-color', '#F3BE89');
        // Show modal
        $('#auth-error-modal').modal('show');
        return;
    }
}

(function () {
    $(document).ready(function () {
        // Prepare the page
        preparePage(function () {
            const info = getUserInfo();
        });
    });
})();