# RxRecyclerViewArrayListAdapter

**Description:**

 An RxKotlin and generics based ArrayList designed to simplify the implementation of RecyclerViews to a single line.
 It also handles data updates by automatically refreshing the RecyclerView whenever data is added or removed.

**Installation:**

 Via Gradle:

 In your app build.gradle:

    compile "com.github.Meeks91:RxRecyclerViewArrayListAdapter:0.25"
    compile "com.android.support:recyclerview-v7:26.0.1"

In your project's build.gradle:

    allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
        }
     }

**Usage:**

1. Create an instance of RxRecyclerViewArrayList to use as the data source.

    Put the data type (e.g. String) you want in the diamond brackets.
    val rxArrayList = RxRecyclerViewArrayList<DataType>()

2. Binding the instance of the RxRecyclerViewArrayList to the RecyclerView.

    Call bind() on the rxArrayList. Add your viewHolderClass to the diamond brackets. Then pass in: your RecyclerView, layoutItemResource, LayoutConfig, and column spanCount.
    ```
    rxArrayList.bind<customViewHolder>(recycylerView, R.layout.item,
                                            LayoutConfig(Orientation.vertical,RowType.single, 1))
                                                        { customViewHolder, dataType ->

      //here your customViewHolder will be available along with the dataType you put in the diamond brackets of the rxArrayList
    }
      ```
The LayoutConfig above acts like a LayoutManager. It takes 3 parameters:

    - The orientation for the rows.
    - The rowType which can be: single (like a LinearLayoutManager), grid (like a GridLayoutManager), or staggered (like a StaggeredGridLayoutManager).
    - The spanCount for the rows. This will only be used for the grid or staggered

3. Updating the the RecylerView:

    To update the RecyclerView you just update the RxRecyclerViewArrayList  by calling the standard ArrayList add(), addAll() and remove() methods on it and the RecylerView will automatically update.

    For example, to add data to the RecyclerView call: `rxArrayList.add(DataType())`
