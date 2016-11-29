using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

/*
 * Created- Aruna Duraisingam  
 * Date: 22/11/2016
 * Invoice model to handle invoice data.
 */

namespace Aruna_technicalTest.Models 
{
    public class InvoiceModel 
    {
        public InvoiceModel() {}

        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        [Display(Name = "Invoice ID")]
        public int ID { get; set;}

        [DataType(DataType.Date)]
        [Required(ErrorMessage = "Enter the TaxPointDate date.")]
        [Display(Name = "Tax Point Date")]
        [DisplayFormat(DataFormatString = "{0:dd-MM-yyyy}", ApplyFormatInEditMode = true)]
        public DateTime? TaxPointDate { get; set; }


        [Required]
        [StringLength(50)]
        [Display(Name = "Reference")]
        public string Reference { get; set; }

        [Required]
        [Display(Name = "Total Gross")]
        [DisplayFormat(DataFormatString = "{0:C}")]
        public double TotalGross { get; set; }

        [Required]
        [StringLength(50)]
        [Display(Name = "Name")]
        public string Name { get; set; }

        [Required]
        [StringLength(50)]
        [Display(Name = "Address")]
        public string Address { get; set; }

    }
}