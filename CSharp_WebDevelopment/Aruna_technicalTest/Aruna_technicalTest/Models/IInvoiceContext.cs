using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

/*
 * Created- Aruna Duraisingam  
 * Date: 22/11/2016
 * Interface to handle dependancy injection for Entity framework.
 */

namespace Aruna_technicalTest.Models
{
    public interface IInvoiceContext : IDisposable
    {
        DbSet<InvoiceModel> InvoiceList { get; }
        int SaveChanges();
        void MarkAsModified(InvoiceModel item);
    }
}
