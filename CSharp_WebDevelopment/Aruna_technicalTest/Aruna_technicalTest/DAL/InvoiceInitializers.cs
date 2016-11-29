using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data.Entity;
using Aruna_technicalTest.Models;

/*
 * Created- Aruna Duraisingam  
 * Date: 22/11/2016
 * Class to initialize the invoice table with dummy data
 */

namespace Aruna_technicalTest.DAL
{
    public class InvoiceInitializers : System.Data.Entity.DropCreateDatabaseIfModelChanges<InvoiceContext>
    {
        protected override void Seed(InvoiceContext context)
        {
            var list = new List<InvoiceModel>
            {
                new InvoiceModel{TaxPointDate=DateTime.Parse("21/09/2015"), Reference = "123", Address= "6102, Eros. Rd", ID=1, Name="Victoria Z. Roger", TotalGross=120.00}
                , new InvoiceModel{TaxPointDate=DateTime.Parse("20/09/2015"), Reference = "QW-12213-c", Address= "6102, Eros. Rd", ID=1, Name="Victoria Z. Roger", TotalGross=96.00}

            };

            list.ForEach(s => context.InvoiceList.Add(s));
            context.SaveChanges();
        }
    }
}