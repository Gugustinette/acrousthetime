export class DateUtil {
  /**
   * Convert a Date from string formatted "YYYYMMDDTHHmmssZ" to a Date object
   * @param date Date from string formatted "YYYYMMDDTHHmmssZ"
   * @returns Date object
   */
  public static convertDate(date: string): Date {
    const dateString = date.substring(
      date.indexOf(':') + 1,
      date.indexOf('Z') + 1,
    );
    const newDate = new Date(
      Number(dateString.substring(0, 4)),
      Number(dateString.substring(4, 6)) - 1,
      Number(dateString.substring(6, 8)),
      Number(dateString.substring(9, 11)),
      Number(dateString.substring(11, 13)),
      Number(dateString.substring(13, 15)),
    );
    return newDate;
  }
}
