#include "libnetchdf_api.h"
#include "stdio.h"

int main(int argc, char** argv) {
    // Obtain reference for calling Kotlin/Native functions
    libnetchdf_ExportedSymbols* lib = libnetchdf_symbols();

    const char* response = lib->kotlin.root.com.sunya.netchdfc.version();
    printf("%s\n", response);
    lib->DisposeString(response);

    ////  libnetchdf_kref_com_sunya_cdm_api_Netchdf (*openNetchdfFileC)(const char* filename);
    const char* filename = "simple_xy.nc";
    libnetchdf_kref_com_sunya_cdm_api_Netchdf netchdf = lib->kotlin.root.com.sunya.netchdfc.openNetchdfFile(filename);

    const char* cdl = lib->kotlin.root.com.sunya.cdm.api.cdl(netchdf);
    printf("file %s\n", filename);
    printf("%s\n", cdl);

    libnetchdf_kref_com_sunya_cdm_api_Variable variable = lib->kotlin.root.com.sunya.cdm.api.Netchdf.findVariable(netchdf, "data");
    libnetchdf_kref_com_sunya_netchdfc_ArrayIntSection arrayIntSection = lib->kotlin.root.com.sunya.netchdfc.readArrayInt(netchdf, variable);

    //  libnetchdf_KInt (*get_totalElems)(libnetchdf_kref_com_sunya_cdm_array_ArrayInt thiz);
    int nelems = lib->kotlin.root.com.sunya.netchdfc.ArrayIntSection.get_nelems(arrayIntSection);
    printf("nelems=%d\n", nelems);

    libnetchdf_kref_kotlin_IntArray shapeIntArray = lib->kotlin.root.com.sunya.netchdfc.ArrayIntSection.get_shape(arrayIntSection);
    int  *shape = (int *) shapeIntArray.pinned;
    printf("first shape=%d\n", *shape);
    printf("second shape=%d\n", *(shape+1));

    // libnetchdf_kref_kotlin_IntArray (*get_values)(libnetchdf_kref_com_sunya_cdm_array_ArrayInt thiz);
    //libnetchdf_kref_kotlin_IntArray intarray = arrayint->get_values(arrayint);
    libnetchdf_kref_kotlin_IntArray intarray = lib->kotlin.root.com.sunya.netchdfc.ArrayIntSection.get_array(arrayIntSection);
    int *data = (int *) intarray.pinned;

    for (int idx=0; idx < 10; idx++) {
        printf(" %d == %d\n", idx, data[idx]);
    }

    lib->DisposeStablePointer(intarray.pinned);
    lib->DisposeStablePointer(arrayIntSection.pinned);
    lib->DisposeStablePointer(variable.pinned);
    lib->DisposeStablePointer(netchdf.pinned);

    return 0;
}