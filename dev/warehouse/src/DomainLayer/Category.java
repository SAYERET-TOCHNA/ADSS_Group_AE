package DomainLayer;


public class Category {
    private String firstCat;
    private String secondCat;
    private String thirdCat;

    public Category(String[] categories){
        if (categories.length != 3){
            throw new IllegalArgumentException("there must be 3 categories");
        }
        firstCat = categories[0];
        secondCat = categories[1];
        thirdCat = categories[2];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return firstCat.equals(category.firstCat) & secondCat.equals(category.secondCat)
                &thirdCat.equals(category.thirdCat);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public String getFirstCat() {
        return firstCat;
    }

    public void setFirstCat(String firstCat) {
        this.firstCat = firstCat;
    }

    public String getSecondCat() {
        return secondCat;
    }

    public void setSecondCat(String secondCat) {
        this.secondCat = secondCat;
    }

    public String getThirdCat() {
        return thirdCat;
    }

    public void setThirdCat(String thirdCat) {
        this.thirdCat = thirdCat;
    }

    @Override
    public String toString() {
        return firstCat + '|' +
                secondCat + '|' +
                thirdCat + '|'
                ;
    }
}
