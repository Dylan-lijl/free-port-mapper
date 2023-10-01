import { downloadBlob } from '@/util/DownloadFile';

export function exportFile(arr, filename = "tmp.json") {
  if (!filename.endsWith('.json')) {
    filename = filename + '.json';
  }
  downloadBlob(new Blob([JSON.stringify(arr)],{type:"text/json;charset=utf-8"}), filename);
}