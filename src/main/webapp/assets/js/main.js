jQuery(document).ready(($) => {
  // Confirmation for Delete buttons
  $(document).on("submit", "form.delete-item-form", function (ev) {
    return confirm("Do really want to delete this item?");
  });
});
