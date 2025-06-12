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

  return 0;
}