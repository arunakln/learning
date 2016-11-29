using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data.Entity;
using System.Data.Entity.ModelConfiguration.Conventions;
using Aruna_technicalTest.Models;

/*
 * Created- Aruna Duraisingam  
 * Date: 22/11/2016
 * Invoice Context to handle InvoiceModel data set. Interface
 * IInvoiceContext implemented to handle the dependency injection
 */

namespace Aruna_technicalTest.DAL
{
    public class InvoiceContext : DbContext, IInvoiceContext
    {
        public InvoiceContext() : base("DefaultConnection") { }


        public DbSet<InvoiceModel> InvoiceList { get; set; }

        public void MarkAsModified(InvoiceModel item)
        {
            Entry(item).State = EntityState.Modified;
        }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            modelBuilder.Conventions.Remove<PluralizingTableNameConvention>();
        }
    }
}