  constructor() {
    this.elements = {};
    this.currentSort = { field: null, direction: "asc" };
    this.selectedItems = new Set();
    this.currentPage = 1;
   */
  handleSort(field) {
    if (this.currentSort.field === field) {
      this.currentSort.direction =
        this.currentSort.direction === "asc" ? "desc" : "asc";
    } else {
      this.currentSort.field = field;
      this.currentSort.direction = "asc";
    }

    this.updateSortIndicators();
    this.dispatchEvent("table:sort", this.currentSort);
  }


    // Adicionar classe ao header atual
    if (this.currentSort.field) {
      const header = document.querySelector(
        `[data-sort="${this.currentSort.field}"]`
      );
      if (header) {
        header.classList.add("sorted");
        if (this.currentSort.direction === "desc") {
          header.classList.add("desc");
        }
      page: this.currentPage,
      size: this.itemsPerPage,
      sort: this.currentSort.field
        ? `${this.currentSort.field},${this.currentSort.direction}`
        : null,
    };