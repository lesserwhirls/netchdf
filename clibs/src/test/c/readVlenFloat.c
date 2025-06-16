/*
export LD_LIBRARY_PATH="/usr/lib/x86_64-linux-gnu"
gcc -o testClib testClib.c -lnetcdf
*/

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <netcdf.h>

/* This is the name of the data file we will read. */
#define FILE_NAME "/home/all/testdata/netchdf/castel/20110421-153623-snippet-VI_MB7125_01.sni"

/* We are reading 2D data, a 6 x 12 grid. */
#define NX 6
#define NY 12

/* Handle errors by printing an error message and exiting with a
 * non-zero status. */
#define ERRCODE 2
#define ERR(e) {printf("Error: %s\n", nc_strerror(e)); exit(ERRCODE);}

int
main()
{
   /* This will be the netCDF ID for the file and data variable. */
   int ncid, grpid, varid;

   /* Loop indexes, and error handling. */
   int retval;

   /* Open the file. NC_NOWRITE tells netCDF we want read-only access
    * to the file.*/
   if ((retval = nc_open(FILE_NAME, NC_NOWRITE, &ncid)))
      ERR(retval);

   /* Get the varid of the data variable, based on its name. */
   if ((retval = nc_inq_varid(ncid, "mbReflectivity", &varid)))
      ERR(retval);
    printf("*** nc_inq_varid =%d\n", varid);

   /* get the attribute unit. */
   int type;
   long attlen;
   if ((retval = nc_inq_att(grpid, varid, "units", &type, &attlen)))
      ERR(retval);
   printf("*** nc_inq_att type=%d attlen = %ld\n", type, attlen);
   /* expect type = 12, size = 1 */

   char **string_attr = (char**)malloc(attlen * sizeof(char*));
   memset(string_attr, 0, attlen * sizeof(char*));

   if ((retval = nc_get_att_string(grpid, varid, "units", string_attr)))
      ERR(retval);
   for (size_t k = 0; k < attlen; ++k) {
     printf("   CAPE:units[%ld] = '%s'\n", k, string_attr[k]);
   }

   if ((retval = nc_free_string(attlen, string_attr)))
        ERR(retval);
   free(string_attr);

   /* Close the file, freeing all resources. */
   if ((retval = nc_close(ncid)))
      ERR(retval);

   printf("*** SUCCESS reading example file %s!\n", FILE_NAME);
   return 0;
}