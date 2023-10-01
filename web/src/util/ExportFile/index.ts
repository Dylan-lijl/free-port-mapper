import CsvExport from './exports/CsvExport';
import ExcelExport from './exports/ExcelExport';
import TxtExport from './exports/TxtExport';

export const csvExport = CsvExport;
export const excelExport = ExcelExport;
export const txtExport = TxtExport;
export const ConvertorType = {
  MAP: 1,
  DATETIME: 2,
  PREFIX: 3,
  SUFFIX: 4,
  ARRAY_TO_STRING: 5
}

export default {
  csvExport,
  excelExport,
  txtExport,
}
