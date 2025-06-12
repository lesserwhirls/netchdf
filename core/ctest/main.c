#include "libnetchdf_api.h"
#include "stdio.h"

int main(int argc, char** argv) {
  // Obtain reference for calling Kotlin/Native functions
  libnetchdf_ExportedSymbols* lib = libnetchdf_symbols();

  const char* filename = "simple_xy.nc";

  libnetchdf_kref_com_sunya_cdm_api_Netchdf netchdf = lib->kotlin.root.com.sunya.netchdf.openNetchdfFile(filename, 0);

  const char* cdl = lib->kotlin.root.com.sunya.cdm.api.cdl(netchdf);

  printf("file %s\n", filename);
  printf("%s\n", cdl);

  libnetchdf_kref_com_sunya_cdm_api_Variable variable = lib->kotlin.root.com.sunya.cdm.api.Netchdf.findVariable(netchdf, "data");

  libnetchdf_kref_com_sunya_cdm_array_ArrayInt arrayint =
    (libnetchdf_kref_com_sunya_cdm_array_ArrayInt) lib->kotlin.root.com.sunya.cdm.api.Netchdf.readArrayDataAll(netchdf, variable);

  libnetchdf_kref_kotlin_IntArray intarray = lib->kotlin.root.com.sunya.cdm.array.ArrayInt.get_values(arrayint);
  int *data = (int *) intarray.pinned;

  printf("%d\n", *data);

  return 0;
}