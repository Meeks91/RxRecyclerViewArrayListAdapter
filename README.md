# RxRecyclerViewArrayListAdapter

**Description:**

 An RxKotlin and generics based ArrayList designed to simplify the implementation of RecyclerViews.
 It handles data updates by automatically refreshing the RecyclerView whenever data is added or removed,
 and automatically applies the appropriate kind of adapter update to ensure that it animates the change for you correctly.

**Installation:**

 Via Gradle:

 In your app build.gradle:

    compile "com.github.Meeks91:RxRecyclerViewArrayListAdapter:0.26"
    compile "com.android.support:recyclerview-v7:26.0.1"

In your project's build.gradle:

    allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
        }
     }

**Usage:**

1 -> Create an instance of RxRecyclerViewArrayList to use as the data source and put the data type (e.g. String) you want in the diamond brackets.

    val rxArrayList = RxRecyclerViewArrayList<DataType>()
 
2 -> Call bind() on the rxArrayList. Add your viewHolderClass to the diamond brackets. Then pass in: your RecyclerView, layoutItemResource and LayoutConfig.

```
 rxArrayList.bind<customViewHolder>(recycylerView, R.layout.item,
                                         LayoutConfig(Orientation.vertical,RowType.single, 1))
                                                 { customViewHolder, dataType ->

       //here your customViewHolder will be available along with
       //the dataType you put in the diamond brackets of the rxArrayList
    }
  ```
The LayoutConfig above acts like a LayoutManager. It takes 3 parameters:

    - An Orientation enum which sets the orientation of the rows. It can either be set to Orientation.vertical
      or Orientation.horizontal.
    - The RowType enum which can be: RowType.single (like a LinearLayoutManager),
      RowType.grid (like a GridLayoutManager), or RowType.staggered (like a StaggeredGridLayoutManager).
    - The spanCount for the rows. Note: This will only be used for the RowType.grid or RowType.staggered
      RowTypes.

3 -> To update the RecyclerView you just update the RxRecyclerViewArrayList  by calling the standard ArrayList add(), addAll() and remove() methods on it and the RecylerView will automatically update.
For example, to add data to the RecyclerView call: `rxArrayList.add(DataType())`.

That means you just have to update the rxArrayList and the RecyclerView will update itself automatically.
