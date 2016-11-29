using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(Aruna_technicalTest.Startup))]
namespace Aruna_technicalTest
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
